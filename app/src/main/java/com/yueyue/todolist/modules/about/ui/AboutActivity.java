package com.yueyue.todolist.modules.about.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.Constants;
import com.yueyue.todolist.common.utils.StatusBarUtil;
import com.yueyue.todolist.common.utils.VersionUtil;
import com.yueyue.todolist.component.Sharer;
import com.yueyue.todolist.modules.personal.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;
import moe.feng.alipay.zerosdk.AlipayZeroSdk;

/**
 * author : yueyue on 2018/3/14 09:38
 * desc   :
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImmersiveStatusBar(this);
        StatusBarUtil.setImmersiveStatusBarToolbar(mToolbar, this);
        initView();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_about;
    }


    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        mTvVersion.setText(String.format("当前版本: %s (Build %s)", VersionUtil.getVersion(this), VersionUtil.getVersionCode(this)));
        mToolbarLayout.setTitleEnabled(false);
        mToolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.bt_code, R.id.bt_blog, R.id.bt_pay, R.id.bt_share, R.id.bt_personal, R.id.bt_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_code:
                goToHtml(getString(R.string.app_html));
                break;
            case R.id.bt_blog:
                goToHtml(getString(R.string.blog_html));
                break;
            case R.id.bt_pay:
                donate();
                break;
            case R.id.bt_share:
                Sharer.shareText(AboutActivity.this,
                        getString(R.string.share_app),
                        getString(R.string.share_txt));
                break;
            case R.id.bt_personal:
                PersonalActivity.launch(AboutActivity.this);
                finish();
                break;
            case R.id.bt_update:
                VersionUtil.checkVersion(this, true);
                break;
        }
    }

    private void donate() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        if (AlipayZeroSdk.hasInstalledAlipayClient(Utils.getApp())) {
                            ToastUtils.showShort(Utils.getApp().getResources().getText(R.string.transfer_to_author));
                            AlipayZeroSdk.startAlipayClient(AboutActivity.this, Constants.ALI_PAY);
                        } else {
                            ToastUtils.showShort(Utils.getApp().getResources().getText(R.string.alipay_not_found));
                        }

                    } else {
                        ToastUtils.showShort(getString(R.string.permission_statement));
                    }
                });
    }

    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}

