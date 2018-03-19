package com.yueyue.todolist.modules.viewer;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.BitmapUtils;
import com.yueyue.todolist.common.utils.Util;
import com.yueyue.todolist.component.cache.BitMapHelper;
import com.yueyue.todolist.modules.edit.domain.ImageEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/3/18 09:03
 * desc   :
 */

public class ViewerActivity extends BaseActivity {

    public static final String VIEWER_IMAGE_ENTITY = "viewer_image_entity";


    @BindView(R.id.pv_viewer_preview)
    PhotoView mPvPreview;

    @BindView(R.id.tv_viewer_save)
    TextView mTvSave;

    @BindView(R.id.tv_viewer_del)
    TextView mTvDel;

    @BindView(R.id.ll_viewer_bottom_bar)
    LinearLayout mLlBottomBar;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

    private BitMapHelper mBitMapHelper;
    private Bitmap mBitmap;
    private List<Disposable> mDisposableList = new ArrayList<>();
    private boolean mIsFullPreView = false;
    private ImageEntity mImageEntity;

    public static void launch(Context context, ImageEntity imageEntity) {
        Intent intent = new Intent(context, ViewerActivity.class);
        if (imageEntity != null) {
            intent.putExtra(VIEWER_IMAGE_ENTITY, imageEntity);
        }
        context.startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initData();
    }


    private void initToolbar() {
        setToolbarTitle(getString(R.string.photo));
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        }
    }

    private void initData() {
        mBitMapHelper = new BitMapHelper(this);

        mImageEntity = getIntent().getParcelableExtra(VIEWER_IMAGE_ENTITY);

        if (mImageEntity != null) {
            mBitmap = mBitMapHelper.get(mImageEntity.imageName);
            initPhotoView(mBitmap);
        }

    }

    private void initPhotoView(Bitmap bitmap) {
        if (bitmap != null) {
            mPvPreview.setImageBitmap(bitmap);
        }
    }

    @OnClick(R.id.tv_viewer_save)
    void applyForStorage() {
        //使用RxPermissions（基于RxJava2） - CSDN博客
        //           http://blog.csdn.net/u013553529/article/details/68948971
        RxPermissions permissions = new RxPermissions(this);
        Disposable disposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        BitmapUtils.saveImageToGallery(ViewerActivity.this, mBitmap);
                    } else {
                        SnackbarUtils
                                .with(mPvPreview)
                                .setMessage(getString(R.string.need_storage_permission_to_save_photo))
                                .setMessageColor(getResources().getColor(R.color.color_ff4081))
                                .setAction(getString(R.string.to_setting),
                                        v -> Util.toAppSetting(ViewerActivity.this))
                                .show();
                    }
                });
        mDisposableList.add(disposable);
    }


    @OnClick(R.id.tv_viewer_del)
    void showDeleteDialog() {
        new AlertDialog.Builder(ViewerActivity.this)
                .setTitle(getString(R.string.delete_photo))
                .setMessage(R.string.are_you_sure_to_delete_the_photo)
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> deleteImage())
                .show();
    }

    private void deleteImage() {
        mBitMapHelper.remove(mImageEntity.imageName);

        Intent intent = new Intent();
        intent.putExtra(VIEWER_IMAGE_ENTITY,mImageEntity);
        setResult(RESULT_OK,intent);
        onBackPressed();
    }

    @OnClick(R.id.pv_viewer_preview)
    void setToolbarAndBottomBarState() {
        if (mIsFullPreView) {
            showToolbarAndBottomBar();
        } else {
            hideToolbarAndBottomBar();
        }

        mIsFullPreView = !mIsFullPreView;
    }

    private void showToolbarAndBottomBar() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAppBarLayout, "translationY",
                -SizeUtils.dp2px(56), 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mLlBottomBar, "translationY",
                SizeUtils.dp2px(56), 0);

        AnimatorSet set = new AnimatorSet();
        set.play(animator1).with(animator2);
        set.setDuration(300);
        set.start();
    }

    private void hideToolbarAndBottomBar() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAppBarLayout, "translationY",
                0, -SizeUtils.dp2px(56));
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mLlBottomBar, "translationY",
                0, SizeUtils.dp2px(56));

        AnimatorSet set = new AnimatorSet();
        set.play(animator1).with(animator2);
        set.setDuration(300);
        set.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
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
