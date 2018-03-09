/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yueyue.todolist.modules.address.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.modules.address.adapter.AddressListAdapter;
import com.yueyue.todolist.modules.address.domain.City;
import com.yueyue.todolist.modules.address.listener.OnCompatItemClickListener;
import com.yueyue.todolist.modules.address.adapter.PagerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yueyue on 2018/3/2 00:52
 * desc   : 选择地址的页面
 * quote  : https://github.com/yanzhenjie/AddressChecker
 */

public class AddressCheckActivity extends AppCompatActivity {

    private static final String KEY_OUTPUT_PROVINCE_CITY_DISTRICT = "KEY_OUTPUT_PROVINCE_CITY_DISTRICT";

    public static String parse(Intent data) {
        return data.getStringExtra(KEY_OUTPUT_PROVINCE_CITY_DISTRICT);
    }

    TabLayout mTabLayout;
    ViewPager mViewPager;

    AddressListAdapter mOneListAdapter;
    List<City> mOneList;
    int mCurrentOneSelect = -1;

    AddressListAdapter mTwoListAdapter;
    List<City> mTwoList;
    int mCurrentTwoSelect = -1;

    AddressListAdapter mThreeListAdapter;
    List<City> mThreeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout.addOnTabSelectedListener(tabSelectedListener);

        RecyclerView oneView = new RecyclerView(this);
        RecyclerView twoView = new RecyclerView(this);
        RecyclerView threeView = new RecyclerView(this);
        List<RecyclerView> recyclerViewList = new ArrayList<>();
        recyclerViewList.add(oneView);
        recyclerViewList.add(twoView);
        recyclerViewList.add(threeView);
        PagerViewAdapter<RecyclerView> pagerViewAdapter = new PagerViewAdapter<>(recyclerViewList);
        mViewPager.setAdapter(pagerViewAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(R.string.select_please);

        oneView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        oneView.setLayoutManager(new LinearLayoutManager(this));
        mOneListAdapter = new AddressListAdapter(getLayoutInflater(), mProvinceItemClickListener);
        oneView.setAdapter(mOneListAdapter);

        twoView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        twoView.setLayoutManager(new LinearLayoutManager(this));
        mTwoListAdapter = new AddressListAdapter(getLayoutInflater(), mCityItemClickListener);
        twoView.setAdapter(mTwoListAdapter);

        threeView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        threeView.setLayoutManager(new LinearLayoutManager(this));
        mThreeListAdapter = new AddressListAdapter(getLayoutInflater(), mDistrictItemClickListener);
        threeView.setAdapter(mThreeListAdapter);

        RequestCityListTask requestCityTask = new RequestCityListTask(this, callback);
        requestCityTask.execute();
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {

        private int mCurrentPosition;

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int newPosition = tab.getPosition();
            switch (newPosition) {
                case 1: {
                    if (mTwoList == null) {
                        mTabLayout.getTabAt(mCurrentPosition).select();
                        return;
                    }
                    break;
                }
                case 2: {
                    if (mThreeList == null) {
                        mTabLayout.getTabAt(mCurrentPosition).select();
                        return;
                    }
                    break;
                }
            }
            this.mCurrentPosition = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    /**
     * 请求服务器数据回来。(我这里是从asset中的json中读取的，模拟从服务器请求。)
     */
    private RequestCityListTask.Callback callback = new RequestCityListTask.Callback() {
        @Override
        public void callback(List<City> cities) {
            mOneList = cities;
            mOneListAdapter.notifyDataSetChanged(mOneList);
        }
    };

    /**
     * 省的item被点击。
     */
    private OnCompatItemClickListener mProvinceItemClickListener = new OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mCurrentOneSelect == position) {
                mViewPager.setCurrentItem(1, true);
                return;
            }
            if (mCurrentOneSelect != -1) {
                mOneList.get(mCurrentOneSelect).setSelect(false);
                mOneListAdapter.notifyItemChanged(mCurrentOneSelect);
            }

            mCurrentOneSelect = position;
            mOneList.get(mCurrentOneSelect).setSelect(true);
            mOneListAdapter.notifyItemChanged(mCurrentOneSelect);

            City one = mOneList.get(mCurrentOneSelect);
            mTwoList = one.getCityList();
            if (mTwoList == null || mTwoList.size() == 0) { // 选定一级。
                setResultFinish(one, null, null);
            } else {
                // 更新二级的content和title。
                mTwoListAdapter.notifyDataSetChanged(mTwoList);
                mTabLayout.getTabAt(1).setText(one.getName());
                mViewPager.setCurrentItem(1, true);

                // 三级置空。
                mTabLayout.getTabAt(2).setText(null);
                mThreeList = null;
                mCurrentTwoSelect = -1;
            }
        }
    };

    /**
     * 市的item被点击。
     */
    private OnCompatItemClickListener mCityItemClickListener = new OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mCurrentTwoSelect == position) {
                mViewPager.setCurrentItem(2, true);
                return;
            }

            if (mCurrentTwoSelect != -1) {
                mTwoList.get(mCurrentTwoSelect).setSelect(false);
                mTwoListAdapter.notifyItemChanged(mCurrentTwoSelect);
            }

            mCurrentTwoSelect = position;
            mTwoList.get(mCurrentTwoSelect).setSelect(true);
            mTwoListAdapter.notifyItemChanged(mCurrentTwoSelect);

            City two = mTwoList.get(mCurrentTwoSelect);
            mThreeList = two.getCityList();
            if (mThreeList == null || mThreeList.size() == 0) { // 选定二级。
                setResultFinish(mOneList.get(mCurrentOneSelect), two, null);
            } else {
                mThreeListAdapter.notifyDataSetChanged(mThreeList);
                mTabLayout.getTabAt(2).setText(two.getName());
                mViewPager.setCurrentItem(2, true);
            }
        }
    };

    /**
     * 区的item被点击。
     */
    private OnCompatItemClickListener mDistrictItemClickListener = new OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            setResultFinish(mOneList.get(mCurrentOneSelect), mTwoList.get(mCurrentTwoSelect), mThreeList.get(position));
        }
    };

    /**
     * 选中。
     */
    private void setResultFinish(City province, City city, City district) {
        ArrayList<City> cityArrayList = new ArrayList<>();
        cityArrayList.add(province);
        if (city != null)
            cityArrayList.add(city);
        if (district != null)
            cityArrayList.add(district);



//        String tvAddress = "", lastId = "";
//        if (cityList != null) {
//            for (int i = 0; i < cityList.size(); i++) {
//                City city = cityList.get(i);
//                lastId = city.getId();
//                tvAddress += city.getName();
//            }
//        }

        String post=null;
        String districtName = district.getName();
        if ("其它区".equals(districtName) || "经济技术开发区".equals(districtName)) {
            ToastUtils.showShort("无法识别,请选择其他区域");
            return;
        }


        Intent intent = new Intent();
        intent.putExtra(KEY_OUTPUT_PROVINCE_CITY_DISTRICT, districtName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
