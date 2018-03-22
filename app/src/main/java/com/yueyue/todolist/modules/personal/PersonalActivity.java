package com.yueyue.todolist.modules.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.C;
import com.yueyue.todolist.component.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : yueyue on 2018/3/22 12:26
 * desc   :
 */

public class PersonalActivity extends BaseActivity {

    @BindView(R.id.iv_author)
    ImageView mIvAuthor;
    @BindView(R.id.tvContacts)
    TextView mTvContacts;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvBlog)
    TextView mTvBlog;
    @BindView(R.id.tvGithub)
    TextView mTvGithub;
    @BindView(R.id.tvEmail)
    TextView mTvEmail;
    @BindView(R.id.tvUrl)
    TextView mTvUrl;
    @BindView(R.id.tvGithubUrl)
    TextView mTvGithubUrl;
    @BindView(R.id.tvEmailUrl)
    TextView mTvEmailUrl;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, PersonalActivity.class));
    }


    @Override
    protected int initLayoutId() {
        return R.layout.activity_personal;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        ImageLoader.LoadImage(this, C.AVATARS_PERSONAL_URL, mIvAuthor,
                new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL));
        Typeface mtypeface = Typeface.createFromAsset(getAssets(), "font/consolab.ttf");
        mTvContacts.setTypeface(mtypeface);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/consola.ttf");
        mTvName.setTypeface(typeface);
        mTvBlog.setTypeface(typeface);
        mTvGithub.setTypeface(typeface);
        mTvEmail.setTypeface(typeface);
        mTvGithubUrl.setTypeface(typeface);
        mTvUrl.setTypeface(typeface);
        mTvEmailUrl.setTypeface(typeface);
    }

    @OnClick({R.id.tvUrl, R.id.tvGithubUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvUrl:
                toWeb(getResources().getString(R.string.willUrl));
                break;
            case R.id.tvGithubUrl:
                toWeb(getResources().getString(R.string.githubUrl));
                break;

        }
    }

    private void toWeb(String url) {
        Uri weburl = Uri.parse(url);
        Intent web_Intent = new Intent(Intent.ACTION_VIEW, weburl);
        startActivity(web_Intent);
    }
}
