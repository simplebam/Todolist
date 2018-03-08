package com.yueyue.todolist.modules.main.ui;


import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseFragment;
import com.yueyue.todolist.common.Constants;
import com.yueyue.todolist.common.utils.SpUtil;

import butterknife.BindView;

/**
 * create an instance of this fragment.
 */
public abstract class RecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    boolean firstFetch;   //whether is first time to enter fragment
    String imageType;               // imageType of recyclerView's content
    int lastPosition;       //last visible position
    int firstPosition;      //first visible position

//    @Override
//    protected int initLayoutId() {
//        return R.layout.fragment_recycler;
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        //android--SwipeRefreshLayout 设置下拉刷新进度条颜色变化没效果 - qq_33703877的博客 - CSDN博客
        //        http://blog.csdn.net/qq_33703877/article/details/54692461
        mSwipeRefresh.setColorSchemeColors(getColor(R.color.colorPrimary),
                getColor(R.color.colorPrimaryDark), getColor(R.color.colorAccent)); //设置进度的颜色

        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        int viewPosition = SpUtil.getInt(imageType + Constants.VIEW_POSITION);
        if (viewPosition > 0) {
            mRecyclerView.smoothScrollToPosition(viewPosition);
            SpUtil.remove(imageType + Constants.VIEW_POSITION);
        }
    }

    public void changeRefresh(final boolean refreshState) {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(refreshState);
        }
    }

    public int getColor(int resId) {
        return ResourcesCompat.getColor(getResources(), resId, null);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


}