package com.yueyue.todolist.base;

/**
 * author : yueyue on 2018/3/4 21:42
 * desc   :
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yueyue.todolist.BuildConfig;

import butterknife.ButterKnife;

/**
 * author : yueyue on 2018/3/2 00:11
 * desc   : BaseFragment helps onCreateView, and initViews(when root is null), init data on Activity Created.
 * quote  : https://github.com/DanteAndroid/Beauty
 */


public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(initLayoutId(), container, false);
            initViews();
        }
        ButterKnife.bind(this, rootView);
        onCreateView();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            toolbar = ((BaseActivity) getActivity()).toolbar;
        }
    }

    protected abstract void initViews();

    protected abstract void initData();


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