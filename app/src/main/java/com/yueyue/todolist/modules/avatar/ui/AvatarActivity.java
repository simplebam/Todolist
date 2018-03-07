package com.yueyue.todolist.modules.avatar.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.component.PLog;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AvatarActivity extends BaseActivity {

    private static final String TAG = AvatarActivity.class.getSimpleName();

    private static final String CROPPED_IMAGE_NAME = "CropImage";
    public static final String AVATAR_FILE_NAME = "avatar.png";

    //拍摄
    private static final int TAKE_PHOTO_REQUEST_CODE = 0x01;
    //选择图库
    private static final int SELECT_GALLERY_REQUEST_CODE = 0x02;

    @BindView(R.id.container)
    ViewGroup mContainer;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.btn_select_gallery)
    Button mBtnSelectGallery;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;


    private Uri mTakePhotoUri;

    private List<Disposable> mDisposableList = new ArrayList<>();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AvatarActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_avatar;
    }


    private void initToolbar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.height = BarUtils.getActionBarHeight();
            toolbar.setLayoutParams(layoutParams);
        }

        setToolbarTitle(getString(R.string.change_avatar));

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(ConvertUtils.dp2px(2));
        gd.setColor(getResources().getColor(R.color.colorPrimary));
        mBtnTakePhoto.setBackground(gd);
        mBtnSelectGallery.setBackground(gd);
    }


    @OnClick(R.id.btn_take_photo)
    public void takePhoto(View view) {
        File outputImageFile = null;
        try {
            //创建File对象,用于存储拍照后的照片
            outputImageFile = createNewFile(AVATAR_FILE_NAME);
        } catch (IOException e) {
            PLog.e(TAG, "takePhoto:" + e.toString());
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            mTakePhotoUri = FileProvider.getUriForFile(AvatarActivity.this,
                    "com.yueyue.todolist.fileProvider", outputImageFile);
        } else {
            mTakePhotoUri = Uri.fromFile(outputImageFile);
        }

        //启动相机程序
        useCamera(mTakePhotoUri);

    }

    /**
     * 打卡系统照相机
     */
    private void useCamera(@NonNull Uri desUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    @OnClick(R.id.btn_select_gallery)
    public void selectGallery(View view) {
        //使用RxPermissions（基于RxJava2） - CSDN博客
        //           http://blog.csdn.net/u013553529/article/details/68948971
        RxPermissions permissions = new RxPermissions(this);
        Disposable disposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        openAlbum();
                    } else {
                        ToastUtils.showShort(getString(R.string.need_storge_permission_to_open_album));
                    }
                });
        mDisposableList.add(disposable);

    }


    /**
     * 打开系统图库
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //查看类型 String IMAGE_UNSPECIFIED = "image/*";
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_GALLERY_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE:
                    mIvAvatar.setImageURI(mTakePhotoUri);
                    setResult(RESULT_OK);
                    break;
                case SELECT_GALLERY_REQUEST_CODE:
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4 以上使用这个办法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                default:
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }

    }


    @TargetApi(19)
    private void handleImageOnKitKat(@NonNull Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (uri == null) return;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        startCropActivity(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        startCropActivity(imagePath); // 根据图片路径显示图片
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void startCropActivity(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) return;

        String destinationFileName = CROPPED_IMAGE_NAME + ".png";
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(imagePath)), Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = uCrop.withAspectRatio(1, 1); //正方形比例
        uCrop = uCrop.withMaxResultSize(1920, 1080); //限制最大size

        UCrop.Options options = new UCrop.Options();
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        options.setActiveWidgetColor(colorPrimary);
        options.setStatusBarColor(colorPrimary);
        options.setToolbarColor(colorPrimary);
        uCrop.withOptions(options);
        uCrop.start(AvatarActivity.this, UCrop.REQUEST_CROP);
    }

    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvAvatar.setImageBitmap(bitmap);
        } else {
            ToastUtils.showShort(getString(R.string.fail_to_get_image));
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        Uri imageUri = UCrop.getOutput(result);
        if (imageUri == null) {
            return;
        }

        if (imageUri.getScheme().equals("file")) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                File saveFile = createNewFile(AVATAR_FILE_NAME);
                fos = new FileOutputStream(saveFile);
                fis = new FileInputStream(new File(imageUri.getPath()));

                FileChannel inChannel = fis.getChannel();
                FileChannel outChannel = fos.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                displayImage(imageUri.getPath());
                setResult(RESULT_OK);
            } catch (Exception e) {
                PLog.e(TAG, "handleCropResult:" + e.toString());
                e.printStackTrace();
            } finally {
                closeSately(fis);
                closeSately(fos);
            }
        } else {
            ToastUtils.showShort(getString(R.string.cannot_get_crop_pic));
        }
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        String msg = cropError != null ? cropError.getMessage() : getString(R.string.unexpected_error);
        ToastUtils.showShort(msg);
    }

    @NonNull
    private File createNewFile(String fileName) throws IOException {
        File saveFile = new File(getExternalCacheDir(), fileName);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        saveFile.createNewFile();
        return saveFile;
    }

    private void closeSately(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
