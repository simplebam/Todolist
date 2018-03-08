package com.yueyue.todolist.modules.main.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueyue.todolist.R;
import com.yueyue.todolist.common.Constants;
import com.yueyue.todolist.modules.main.adapter.DiaryHomeAdapter;
import com.yueyue.todolist.modules.main.db.DiaryStore;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author : yueyue on 2018/3/8 00:02
 * desc   :
 */

public class DiaryHomeFragment extends RecyclerFragment {

    private static final String TAG = DiaryHomeFragment.class.getSimpleName();
    private static final String TYPE_TODAY = "today";
    private static final String TYPE_TOMORROW = "tomorrow";

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private int dbOffset = 0; //分页查询的起始值
    private List<DiaryEntity> mDiaryList;
    private DiaryHomeAdapter mDiaryHomeAdapter;

    public static DiaryHomeFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constants.TYPE, type);
        DiaryHomeFragment fragment = new DiaryHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mFab.setOnClickListener((v)->{

        });
    }

    //今天,明天,所有,已经完成
    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString(Constants.TYPE);
            switch (type) {
                case TYPE_TODAY:
                    mDiaryList = DiaryStore.getInstance().getTodayAllDiary(dbOffset);
                    break;
                case TYPE_TOMORROW:
                    mDiaryList = DiaryStore.getInstance().getTomorrowAllDiary(dbOffset);
                    break;
                default:
                    mDiaryList = new ArrayList<>();
                    break;
            }
        }


        mDiaryHomeAdapter = new DiaryHomeAdapter(R.layout.fragment_diary_home_item, mDiaryList);

        mDiaryHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        }, mRecyclerView);
        // 没有数据的时候默认显示该布局
//        mDiaryHomeAdapter.setEmptyView(R.layout.fragment_diary_home_item);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_recycler;
    }


}
