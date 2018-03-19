package com.yueyue.todolist.modules.share;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.BitmapUtils;
import com.yueyue.todolist.common.utils.MyFileUtils;
import com.yueyue.todolist.common.utils.Util;
import com.yueyue.todolist.component.Sharer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/3/16 16:41
 * desc   :
 * qoute  : <a href="http://yifeng.studio/2017/03/21/android-obtain-view-width-and-heigth-not-zero/">Android 获取 View 宽高的常用正确方式，避免为零 | YiFeng's Zone </a>
 */

public class ShareActivity extends BaseActivity {

    private static final String TAG = ShareActivity.class.getSimpleName();

    public static final String SHARE_PHOTO_NAME = "share.jpg";
    private static final String EXTRA_BITMAP_PATH = "bitmap_path";

    @BindView(R.id.pv_share_preview)
    PhotoView mPvPreview;


    private Bitmap mBitmap;
    private List<Disposable> mDisposableList = new ArrayList<>();

    public static void launch(Context context, String desPath) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(EXTRA_BITMAP_PATH, desPath);
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
        String desPath = getIntent().getStringExtra(EXTRA_BITMAP_PATH);

        if (TextUtils.isEmpty(desPath)) {
            return;
        }

        //Android 获取 View 宽高的常用正确方式，避免为零 | YiFeng's Zone
        //  http://yifeng.studio/2017/03/21/android-obtain-view-width-and-heigth-not-zero/
        mPvPreview.post(() -> {
            int reqWidth = mPvPreview.getWidth();
            int reqHeight = mPvPreview.getHeight();
            mBitmap = BitmapUtils.bitmapResizeFromFile(desPath, reqWidth, reqHeight);
            initPhotoView(mBitmap);
        });
    }


    private void initPhotoView(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        mPvPreview.setImageBitmap(bitmap);
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
                ShareImage();
                break;
            case R.id.menu_share_save:
                applyForStorage();
                break;
        }
        return true;
    }

    private void ShareImage() {
        String desPath = getIntent().getStringExtra(EXTRA_BITMAP_PATH);

        if (!TextUtils.isEmpty(desPath)) {
            sendImage(desPath);
            return;
        }

        //下面这些代码是防止前一个 Activity 保存在本地图片被人为(用户/清理软件)清理,所以再生成一次试试

        if (mBitmap == null) {
            return;
        }

        desPath = MyFileUtils.getDiskPicturesDirFileStr(ShareActivity.this, SHARE_PHOTO_NAME);
        boolean b = MyFileUtils.saveImageToLoacl(mBitmap, Bitmap.CompressFormat.JPEG,
                100, desPath);

        if (!b) {
            ToastUtils.showShort(getString(R.string.unable_to_share_picture_and_try_later));
            return;
        }

        sendImage(desPath);

    }

    private void sendImage(String desPath) {
        Uri uri = Uri.fromFile(new File(desPath));
        Sharer.shareImage(ShareActivity.this, getString(R.string.share), uri);
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
                        BitmapUtils.saveImageToGallery(ShareActivity.this, mBitmap);
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtils.recycleBitamp(mBitmap);

        //释放资源
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }

    }
}
