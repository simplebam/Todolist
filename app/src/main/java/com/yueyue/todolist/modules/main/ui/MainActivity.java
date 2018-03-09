package com.yueyue.todolist.modules.main.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.modules.address.ui.AddressCheckActivity;
import com.yueyue.todolist.modules.diary.ui.AddDiaryActivity;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;
import com.yueyue.todolist.modules.weather.ui.WeatherActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final long DRAWER_CLOSE_DELAY = 230L;
    private static final int ADDRESS_REQUEST_CODE = 666;

    private static final String KEY_GUIDE_BUILD = "guide_build";
    private static final String KEY_GUIDE_SIDE = "guide_side";
    private SparseArray<Fragment> mFragmentSparseArray;


    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.fab)
    FloatingActionButton mFab;


    @BindView(R.id.root)
    CoordinatorLayout root;

    public ActionBarDrawerToggle toggle;
    private boolean backPressed;
    private MenuItem currentMenu;
    private SparseArray<Fragment> fragmentSparseArray;
    private boolean isFirst = true;
    private int placeHolderHeight;
    private boolean secretMode;


    //侧滑栏
    @BindView(R.id.nav_view)
    NavigationView mNavView;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDrawer();
        initToolbar();
//        initFab();
        initNavigationView();
        initListener();


        // FIXME: 2018/3/4 仿照就看天气或者DonateGrils
        //Update();
        // FIXME: 2018/3/4 接上天气/仿照PonyMusic
        //updateWeather();
        initFragments(savedInstanceState);
    }


    private void setupDrawer() {
        toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initToolbar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.height = BarUtils.getActionBarHeight();
            toolbar.setLayoutParams(layoutParams);
        }

    }


    private void initListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
//                showGuideBuild();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initNavigationView() {
        //load headerView's image
        View navHeaderView = mNavView.getHeaderView(0);
//        Imager.load(this, R.drawable.head, head);
        // FIXME: 2018/3/4 到时更新为天气刷新,最多点击三次
        navHeaderView.setOnClickListener(new View.OnClickListener() {
            int index;
            long now;
            long lastTime;

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + now + " - " + lastTime);
                now = new Date().getTime();
                if (now - lastTime < 500) {
                    if (index < 3) {
                        index++;
                    } else {
                        ToastUtils.showShort(R.string.head_view_hint);
                    }
                }
                lastTime = now;
            }
        });
        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_todo:
                DiaryEntity entity = new DiaryEntity();
                entity.time = new Date().getTime();
                AddDiaryActivity.launch(this, AddDiaryActivity.TYPE_ADD, entity);
                break;
            case R.id.menu_calendar:
                break;
            case R.id.menu_finished:
                break;
            case R.id.menu_all:
                break;
            case R.id.menu_weather:
                WeatherActivity.launch(this);
                break;
            case R.id.menu_more_service:
                break;
            case R.id.menu_setting:
                break;
            case R.id.menu_about:
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void initFragments(Bundle savedInstanceState) {
        if (mFragmentSparseArray == null) {
            mFragmentSparseArray = new SparseArray<>();
//            String[] titles = getResources().getStringArray(R.array.main_tabs_titles);
//            String[] types = getResources().getStringArray(R.array.main_tabs_types);
//
//            mFragmentSparseArray.put(R.id.menu_todo, MainTabsFragment.newInstance(titles, types));
//
//            if (secretMode) {
//                //Gank & Douban
//                titles = all;
//                types = new String[]{TYPE_GANK, TYPE_DB_RANK, TYPE_DB_BREAST, TYPE_DB_BUTT, TYPE_DB_LEG, TYPE_DB_SILK};
//            } else {
//                titles = new String[]{all[0]};
//                types = new String[]{TYPE_GANK};
//            }
//
//
//            //二次元
//            titles = getResources().getStringArray(R.array.a_titles);
//            types = new String[]{TYPE_A_ANIME, TYPE_A_FULI, TYPE_A_HENTAI, TYPE_A_UNIFORM, TYPE_A_ZATU};
//            mFragmentSparseArray.put(R.taskId.nav_a, MainTabsFragment.newInstance(titles, types));
//            //favorite
//            mFragmentSparseArray.put(R.taskId.nav_favorite, new FavoriteFragment());

            //weather
//            mFragmentSparseArray.put(R.id.menu_weather, WeatherFragment.newInstance());
        }
//        setMainFragment(R.id.menu_todo, mFragmentSparseArray, savedInstanceState == null);
    }

    /**
     * DrawerLayout侧滑菜单 - summerjing - 博客园
     * https://www.cnblogs.com/blogljj/p/5016588.html?utm_source=tuicool&utm_medium=referral
     */
    public void changeNavigator(boolean enable) {
        if (toggle == null) return;
        if (enable) {
            toggle.setDrawerIndicatorEnabled(true);//true的时候会走系统的逻辑，展示的是系统图标
        } else {
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setToolbarNavigationClickListener(v -> onBackPressed());
        }
    }

    public String getCurrentMenuTitle() {
        if (currentMenu == null) {
            currentMenu = mNavView.getMenu().getItem(0);
        }
        return currentMenu.getTitle().toString();
    }

    public void changeDrawer(boolean enable) {
        int lockMode = enable ? DrawerLayout.LOCK_MODE_UNLOCKED : LOCK_MODE_LOCKED_CLOSED;
        mDrawerLayout.setDrawerLockMode(lockMode);

    }

    @OnClick(R.id.fab)
    void fabClick() {
        String fragmentName = currentFragment.getClass().getSimpleName();
        switch (fragmentName) {
            case "WeatherFragment":
                startActivityForResult(new Intent(this,
                        AddressCheckActivity.class), ADDRESS_REQUEST_CODE);
                break;
            case "0":
                //点击今天
                //            Intent intent = new Intent(MainActivity.this, PlanTaskActivity.class);
//            intent.putExtra(PlanTaskActivity.KEY_SHOW_TYPE, PlanTaskActivity.TYPE_NEW_BUILD);
//            if (mLastSelectedSideId == ID_TOMORROW) {
//                intent.putExtra(PlanTaskActivity.KEY_IS_TOMORROW, true);
//            }
//            startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case ADDRESS_REQUEST_CODE: {
                parseAddress(data);
                break;
            }
        }
    }


    /**
     * 解析地址。
     */
    private void parseAddress(Intent intent) {
        String cityName = AddressCheckActivity.parse(intent);
        if (!TextUtils.isEmpty(cityName)) {
//            SpUtil.getInstance().putCityName(cityName);
//            RxBus.getDefault().post(new ChangeCityEvent(cityName));
        }

    }


    public void setFabVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        mFab.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        doublePressBackToQuit();
    }


    private void doublePressBackToQuit() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }
        backPressed = true;
        SnackbarUtils.with(mDrawerLayout).setMessage(getString(R.string.leave_app));
        new Handler().postDelayed(() -> backPressed = false, 2000);
    }

//    private void showGuideSide() {
//        Controller controller = NewbieGuide.with(this)
//                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
//                    @Override
//                    public void onRemoved(Controller controller) {
//                        mDrawerLayout.openDrawer(GravityCompat.START);
//                    }
//                })
//                .setBackgroundColor(getResources().getColor(R.color.guide_bg_color))
//                .setEveryWhereCancelable(true)
//                .setLayoutRes(R.layout.guide_side_view_layout)
//                .alwaysShow(false)
//                .addHighLight(mHomeAsUp, HighLight.Type.CIRCLE)
//                .setLabel(KEY_GUIDE_SIDE)
//                .build();
//        controller.show();
//    }


//    private void showGuideBuild() {
//        Controller controller = NewbieGuide.with(this)
//                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
//                    @Override
//                    public void onRemoved(Controller controller) {
////                        Intent intent = new Intent(MainActivity.this, PlanTaskActivity.class);
////                        intent.putExtra(PlanTaskActivity.KEY_SHOW_TYPE, PlanTaskActivity.TYPE_NEW_BUILD);
////                        startActivity(intent);
//                    }
//                })
//                .setBackgroundColor(getResources().getColor(R.color.guide_bg_color))
//                .setEveryWhereCancelable(true)
//                .setLayoutRes(R.layout.guide_build_view_layout)
//                .alwaysShow(false)
//                .addHighLight(mFab, HighLight.Type.CIRCLE)
//                .setLabel(KEY_GUIDE_BUILD)
//                .build();
//        controller.show();
//    }


}
