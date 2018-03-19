package com.yueyue.todolist.modules.edit.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.MyFileUtils;
import com.yueyue.todolist.common.utils.ProgressDialogUtils;
import com.yueyue.todolist.component.ImageLoader;
import com.yueyue.todolist.component.PLog;
import com.yueyue.todolist.component.Sharer;
import com.yueyue.todolist.component.cache.BitMapHelper;
import com.yueyue.todolist.modules.edit.domain.ImageEntity;
import com.yueyue.todolist.modules.edit.impl.EditTextWatcherImpl;
import com.yueyue.todolist.modules.viewer.ViewerActivity;
import com.yueyue.todolist.modules.main.domain.NoteEntity;
import com.yueyue.todolist.modules.share.ShareActivity;
import com.yueyue.todolist.widget.MyEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/3/15 09:39
 * desc   :
 */

public class EditNoteActivity extends BaseActivity {

    private static final String TAG = EditNoteActivity.class.getSimpleName();
    private static final String EXTRA_NOTE_DATA = "extra_note_data";
    // 图片距离左右的总距离
    private static final float IMAGE_MARGIN = SizeUtils.dp2px(32);

    //拍摄
    private static final int TAKE_PHOTO_REQUEST_CODE = 0x11;
    //选择图库
    private static final int SELECT_GALLERY_REQUEST_CODE = 0x22;
    //跳转ViewerActivity
    private static final int VIEWER_ACTIVITY_REQUEST_CODE = 0x33;

    @BindView(R.id.scroll_edit_note)
    ScrollView mScrollView;

    @BindView(R.id.et_edit_note_content)
    MyEditText mEtContent;

    @BindView(R.id.ll_edit_note_to_camera)
    LinearLayout mLlToCamera;

    @BindView(R.id.ll_edit_note_to_photo)
    LinearLayout mLlToPhoto;

    private ProgressDialogUtils mProgressDialog = new ProgressDialogUtils(this);

    private List<Disposable> mDisposableList = new ArrayList<>();

    private BitMapHelper mBitMapHelper;

    //文件路径
    private File mImageFile;

    private NoteEntity mNoteEntity;

    public static void launch(Context context, NoteEntity noteEntity) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        if (noteEntity != null) {
            intent.putExtra(EXTRA_NOTE_DATA, noteEntity);
        }
        context.startActivity(intent);
    }


    @Override
    protected int initLayoutId() {
        return R.layout.activity_edit_note;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initmEtContent();
    }


    private void initData() {
        Intent intent = getIntent();
        mNoteEntity = intent.getParcelableExtra(EXTRA_NOTE_DATA);

        if (mNoteEntity == null) {
            mNoteEntity = new NoteEntity();
        }

        mNoteEntity.modifiedTime = TimeUtils.getNowMills();
        setToolbarTitle();

        mBitMapHelper = new BitMapHelper(EditNoteActivity.this);
    }

    private void initmEtContent() {
        setEditTextState(true);

        mEtContent.addTextChangedListener(new EditTextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < mEtContent.mImageList.size(); i++) {
                    ImageEntity imageEntity = mEtContent.mImageList.get(i);
                    if (start == imageEntity.getEnd()) {
                        mEtContent.getEditableText().replace(imageEntity.getStart(), imageEntity.getEnd(), "");
                        mEtContent.mImageList.remove(i);
                        mEtContent.mDeleteImageList.add(imageEntity);
                        break;
                    }
                }
                mEtContent.setTextCountChange(start, before, count);
            }
        });
    }


    public void setToolbarTitle() {
        setToolbarTitle("Title");

        if (toolbar != null) {
            toolbar.setSubtitle("Subtitle");
        }
    }

    @OnClick(R.id.ll_edit_note_to_camera)
    void takePhoto() {
        try {
            //创建File对象,用于存储拍照后的照片
            String imageName = TimeUtils.getNowString() + ".jpg";
            mImageFile = MyFileUtils.createNewFile(getExternalCacheDir(), imageName);
        } catch (IOException e) {
            PLog.e(TAG, "takePhoto:" + e.toString());
            e.printStackTrace();
        }

        if (mImageFile == null) {
            return;
        }


        Uri takePhotoUri;
        if (Build.VERSION.SDK_INT >= 24) {
            takePhotoUri = FileProvider.getUriForFile(EditNoteActivity.this,
                    "com.yueyue.todolist.fileProvider", mImageFile);
        } else {
            takePhotoUri = Uri.fromFile(mImageFile);
        }

        //启动相机程序
        useCamera(takePhotoUri);
    }

    /**
     * 打卡系统照相机
     */
    private void useCamera(@NonNull Uri desUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    @OnClick(R.id.ll_edit_note_to_photo)
    void selectGallery() {
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

    @OnClick(R.id.et_edit_note_content)
    void clickNoteEditText() {
        // 获取光标位置
        int selectionAfter = mEtContent.getSelectionStart();
        Log.i(TAG, "光标位置：" + selectionAfter);

        for (int i = 0; i < mEtContent.mImageList.size(); i++) {

            ImageEntity imageEntity = mEtContent.mImageList.get(i);

            if (selectionAfter >= imageEntity.getStart()
                    && selectionAfter <= imageEntity.getEnd()) { // 光标位置在照片的位置内
                Log.i(TAG, "起点:" + imageEntity.getStart() + "  终点:" + imageEntity.getEnd());
                // 隐藏键盘
                KeyboardUtils.hideSoftInput(EditNoteActivity.this);
                // 光标移到图片末尾的换行符后面
                mEtContent.setSelection(imageEntity.getEnd() + 1);
                startViwerActivity(imageEntity);
                break;
            }
        }
    }

    private void startViwerActivity(ImageEntity imageEntity) {
        ViewerActivity.launch(EditNoteActivity.this, imageEntity);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE:
                    displayImage(mImageFile.getPath());
                    break;
                case SELECT_GALLERY_REQUEST_CODE:
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4 以上使用这个办法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                    break;
                case VIEWER_ACTIVITY_REQUEST_CODE:
                    handleDataFromData(data);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleDataFromData(Intent data) {
        if (data == null) {
            return;
        }

        ImageEntity imageEntity = data.getParcelableExtra(ViewerActivity.VIEWER_IMAGE_ENTITY);
        deleteImage(imageEntity);

    }

    private void deleteImage(ImageEntity imageEntity) {
        mEtContent.getEditableText().replace(imageEntity.getStart(), imageEntity.getEnd() + 1, "");
        mBitMapHelper.remove(imageEntity.imageName);
    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath); // 根据图片路径显示图片
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
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
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

    private void displayImage(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 对图片进行设置 但不形成示例，不耗费内存
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, options);

        int imageReqWidth = getRequestImgWidth();
        int imageReqHeight = getRequestImgHeight(options);

        ImageLoader.load(EditNoteActivity.this, imagePath, imageReqWidth, imageReqHeight,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //根据Bitmap对象创建ImageSpan对象
                        Log.i(TAG, "bitmap width:" + resource.getWidth() + "   height:" + resource.getHeight());
                        insertImage(imagePath, resource);
                    }
                });

    }

    /**
     * @param imageName 使用imagePath作为LruCache以及DiskLruCache的key,key应该使用md5进行编码
     */
    private void insertImage(String imageName, Bitmap bitmap) {
        String imagekey = EncryptUtils.encryptMD5ToString(imageName);
        mEtContent.insertDrawable(bitmap, imagekey);
        mBitMapHelper.put(imagekey, bitmap);
    }

    private int getRequestImgHeight(BitmapFactory.Options options) {
        float width = options.outWidth;
        float height = options.outHeight;
        //
        int reqImgWidth = getRequestImgWidth();
        //计算宽、高缩放率
        float scanleWidth = reqImgWidth / width;
        return (int) (height * scanleWidth);
    }

    private int getRequestImgWidth() {
        return (int) (ScreenUtils.getScreenWidth() - IMAGE_MARGIN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_note_statistics:
                KeyboardUtils.hideSoftInput(this);
                countTextAndImage(mEtContent);
                break;
            case R.id.menu_note_share:
                KeyboardUtils.hideSoftInput(this);
                showShareDialg();
                break;
        }
        return true;
    }

    private void countTextAndImage(MyEditText myEditText) {
        int count = myEditText.getText().length();
        int imageCount = myEditText.mImageList.size();
        for (int i = 0; i < imageCount; i++) {
            count = count - (myEditText.mImageList.get(i).getImageFlag().length());
            // 再减去一个换行符
            count = count - 1;
        }
        showStatisticsDialog(imageCount, count);
    }

    private void showStatisticsDialog(int imageCount, int textCount) {
        StringBuilder sb = new StringBuilder()
                .append("文字数量：")
                .append(textCount)
                .append("\n")
                .append("图片数量：")
                .append(imageCount);

        new AlertDialog.Builder(this)
                .setMessage(sb.toString())
                .setPositiveButton(getString(R.string.sure), null)
                .show();
    }

    /**
     * 显示分享Dialog
     */
    private void showShareDialg() {
        if (mEtContent.getText().toString().length() == 0) {
            return;
        }

        String items[];

        // 没有图片时 添加：以文字形式分享的方法
        if (mEtContent.mImageList.size() == 0) {
            items = new String[]{getString(R.string.share_by_picture), getString(R.string.share_by_text)};
        } else {
            items = new String[]{getString(R.string.share_by_picture)};
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("分享")
                .setItems(items, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            shareNoteWithImage();
                            break;
                        case 1:
                            String content = mEtContent.getText().toString();
                            Sharer.shareText(EditNoteActivity.this, getString(R.string.share), content);
                            break;
                    }
                })
                .show();
    }

    public void shareNoteWithImage() {
        //简单的来说, subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
        // 当执行onDestory()时， 自动解除订阅
        Observable.timer(0, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onSubscribe -> {
                    //为了合成时候没有光标影子
                    setEditTextState(false);
                    mProgressDialog.show(getString(R.string.creating_image));
                })
                .observeOn(Schedulers.io())
                .map(aLong -> createShareBitmap(mEtContent))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(bitmap -> startShareActivity(bitmap))
                .doOnComplete(() -> {
                    ToastUtils.showShort(getString(R.string.success_on_create_image));
                    mProgressDialog.hide();
                    setEditTextState(true);
                })
                .subscribe();
    }

    public Bitmap createShareBitmap(View view) {
        Bitmap bitmap = ImageUtils.view2Bitmap(view);
        int x = bitmap.getWidth() - SizeUtils.sp2px(72);
        int y = bitmap.getHeight() - SizeUtils.sp2px(16);
        int watermarkColor = Utils.getApp().getResources().getColor(R.color.color_89000000);
        String watermark = getString(R.string.from_todolist);
        bitmap = ImageUtils.addTextWatermark(bitmap, watermark, 24, watermarkColor, x, y);
        return bitmap;
    }


    private void startShareActivity(Bitmap bitmap) {
        String desPath = MyFileUtils.getDiskPicturesDirFileStr(EditNoteActivity.this, ShareActivity.SHARE_PHOTO_NAME);
        boolean b = MyFileUtils.saveImageToLoacl(bitmap, Bitmap.CompressFormat.JPEG, 80, desPath);

        if (!b) {
            ToastUtils.showShort(getString(R.string.unable_to_send_picture_and_try_later));
            return;
        }

        bitmap.recycle();
        ShareActivity.launch(EditNoteActivity.this, desPath);
    }


    public void setEditTextState(boolean enabled) {
        mEtContent.setMinHeight(enabled ? getNoteEditNeedHeight() : 0);
        mEtContent.setEnabled(enabled);
    }

    private int getNoteEditNeedHeight() {
        // 屏幕高度减去 状态栏高度、toolbar高度、底部工具栏高度
        return ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight()
                - SizeUtils.dp2px(56) - SizeUtils.dp2px(48);
    }


    @Override
    public void onBackPressed() {
        //et_content.getText().toString().trim()以及mEdContent.getText().toString()
        // 什么都不输入也会返回""字符串
        saveNote(mEtContent.getText().toString());
        super.onBackPressed();
    }

    public void saveNote(String content) {
        // 内容改变时才保存
        if (!content.equals(mNoteEntity.noteContent)) {
            mNoteEntity.modifiedTime = TimeUtils.getNowMills();
            mNoteEntity.noteContent = content;
//            intent.putExtra("position", mPosition);
            setResult(RESULT_OK, getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitMapHelper != null) {
            mBitMapHelper.clear();
        }

        //释放资源
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
