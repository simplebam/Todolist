package com.yueyue.todolist.base;

/**
 * author : yueyue on 2018/3/4 21:42
 * desc   :
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yueyue.todolist.BuildConfig;

import butterknife.ButterKnife;

/**
 * author : yueyue on 2018/3/2 00:11
 * desc   : BaseFragment helps onCreateView, and initViews(when root is null), init data on Activity Created.
 * quote  : https://github.com/DanteAndroid/Beauty
 */

public abstract class BaseFragment extends RxFragment {

    protected View rootView;
    protected Toolbar toolbar;
    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(initLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        initViews();
        onCreateView();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isHidden", isHidden());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract int initLayoutId();

    protected void onCreateView() {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            toolbar = ((BaseActivity) activity).toolbar;
        }
    }

    protected abstract void initViews();

    protected abstract void initData();

//    //RxJava之过滤操作符 - 行云间 - CSDN博客
//    //      http://blog.csdn.net/io_field/article/details/51378909
//    public <T> Observable.Transformer<T, T> applySchedulers() {
//        return observable -> observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .distinct();
//    }

    protected void setToolBarTitle(String title) {
        if (toolbar != null && title != null) {
            toolbar.setTitle(title);
        }
    }

    public void log(String key, String content) {
        if (BuildConfig.DEBUG && getUserVisibleHint()) {
            Log.d(getClass().getSimpleName(), key + "  " + content);
        }
    }

    public void log(String key, int content) {
        log(key, String.valueOf(content));
    }

    public void log(String key) {
        log(key, "");
    }

}