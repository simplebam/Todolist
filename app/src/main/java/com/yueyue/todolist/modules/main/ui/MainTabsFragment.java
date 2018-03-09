package com.yueyue.todolist.modules.main.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainTabsFragment extends BaseFragment {
    private static final int SMOOTH_SCROLL_POSITION = 50;
    private static final String TITLES = "titles";
    private static final String TYPES = "types";
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    private List<BaseFragment> fragments = new ArrayList<>();
    private TabPagerAdapter adapter;

    public static MainTabsFragment newInstance(String[] titles, String[] types) {
        Bundle args = new Bundle();
        args.putStringArray(TYPES, types);
        args.putStringArray(TITLES, titles);
        MainTabsFragment fragment = new MainTabsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_tab;
    }


    @Override
    protected void initViews() {
        adapter = new TabPagerAdapter(getChildFragmentManager());
        initFragments();
        pager.setAdapter(adapter);
        tabs.setTabMode(ScreenUtils.isLandscape() ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void initData() {
//        appBarLayout.animate().alpha(1)
//                .setStartDelay(200).start();
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());
//            }
//        });

    }

    private void initFragments() {
//        String[] titles = getArguments().getStringArray(TITLES);
//        String[] types = getArguments().getStringArray(TYPES);
//        if (types != null && titles != null) {
//            for (String type : types) {
//                fragments.add(DiaryHomeFragment.newInstance(type));
//            }
//            if (fragments.size() != titles.length) {
//                throw new IllegalArgumentException("You need add all fragments! Check: " + getClass().getSimpleName());
//            }
//
//            adapter.setFragments(fragments, titles);
//        }
    }

    private void scrollToTop(RecyclerView list) {
        if (null != list) {
            int lastPosition;
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) list.getLayoutManager();
            lastPosition = manager.findLastVisibleItemPositions(
                    new int[manager.getSpanCount()])[1];

            if (lastPosition < SMOOTH_SCROLL_POSITION) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
    }


    public class TabPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fragments;
        private String[] titles;

        TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void setFragments(List<BaseFragment> fragments, String[] titles) {
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        DiaryHomeFragment getCurrent() {
            return (DiaryHomeFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
        }
    }
}
