package com.yueyue.todolist.modules.share;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.MyFileUtils;
import com.yueyue.todolist.common.utils.Util;
import com.yueyue.todolist.component.PLog;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/3/16 16:41
 * desc   :
 */

public class ShareActivity extends BaseActivity {

    private static final String TAG = ShareActivity.class.getSimpleName();
    private static final String EXTRA_BITMAP_BYTES = "bitmap_bytes";

    @BindView(R.id.pv_share_preview)
    PhotoView mPvPreview;

    private List<Disposable> mDisposableList = new ArrayList<>();

    private Bitmap mBitmap;

    public static void launch(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, ShareActivity.class);
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();//回收
            byte[] bytes = stream.toByteArray();
            intent.putExtra(EXTRA_BITMAP_BYTES, bytes);
            Log.i(TAG, "launch: bytes:"+bytes.length);
        }
        context.startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        initData();
    }


    private void setupView() {
        setToolbarTitle(getString(R.string.preview));
    }

    private void initData() {
        byte[] bytes = getIntent().getByteArrayExtra(EXTRA_BITMAP_BYTES);
        if (bytes != null) {
            mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mPvPreview.setImageBitmap(mBitmap);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share_send:
//                mPresenter.sendBitmap();
                break;
            case R.id.menu_share_save:
                applyForStorage();
                break;
        }
        return true;
    }


    private void applyForStorage() {
        //使用RxPermissions（基于RxJava2） - CSDN博客
        //           http://blog.csdn.net/u013553529/article/details/68948971
        RxPermissions permissions = new RxPermissions(this);
        Disposable disposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        saveImageToGallery(mBitmap);
                    } else {
                        SnackbarUtils
                                .with(mPvPreview)
                                .setMessage(getString(R.string.need_storage_permission_to_save_photo))
                                .setMessageColor(getResources().getColor(R.color.color_ff4081))
                                .setAction(getString(R.string.to_setting),
                                        v -> Util.toAppSetting(ShareActivity.this))
                                .show();
                    }
                });
        mDisposableList.add(disposable);
    }


    //保存文件到指定路径
    private void saveImageToGallery(Bitmap bitmap) {
        // 首先保存图片
        if (!MyFileUtils.isStorageMounted()) {
            ToastUtils.showShort(getString(R.string.unable_to_save_photo_because_of_the_stroage_error));
            return;
        }

        if (bitmap == null) {
            ToastUtils.showShort(getString(R.string.the_photo_not_exist));
            return;
        }


        File parentFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(parentFile, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //通过io流的方式来压缩保存图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
            fos.flush();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            ToastUtils.showShort(getString(R.string.save_success)+":"+file.getPath());
        } catch (IOException e) {
            PLog.e(TAG, "saveImageToGallery: " + e.toString());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    PLog.e(TAG, "saveImageToGallery: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
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
