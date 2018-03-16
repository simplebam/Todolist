package com.yueyue.todolist.modules.main.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.C;
import com.yueyue.todolist.modules.about.ui.AboutActivity;
import com.yueyue.todolist.modules.edit.ui.EditNoteActivity;
import com.yueyue.todolist.modules.main.component.WeatherExecutor;
import com.yueyue.todolist.modules.other.ui.OtherServerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

public class MainActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final long DRAWER_CLOSE_DELAY = 230L;
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
    private boolean isFirst = true;
    private List<Disposable> mDisposableList = new ArrayList<>();

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
        initNavigationView();
        initFragments(savedInstanceState);

        // FIXME: 2018/3/4 仿照就看天气或者DonateGrils
        //Update();
        updateWeather();
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


    @TargetApi(Build.VERSION_CODES.M)
    private void initNavigationView() {
        View navHeaderView = mNavView.getHeaderView(0);
        navHeaderView.setOnClickListener(new View.OnClickListener() {
            int index;
            long now;
            long lastTime;

            @Override
            public void onClick(View v) {
                now = new Date().getTime();
                if (now - lastTime < 1000) {
                    if (index < 3) {
                        index++;
                    } else {
                        ToastUtils.showShort(R.string.head_view_hint);
                    }
                } else {
                    updateWeather();
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
                break;
            case R.id.menu_privacy:
                break;
            case R.id.menu_recycle_bin:
                break;
            case R.id.menu_more_service:
                switchMenu(R.id.menu_more_service, mFragmentSparseArray);
                break;
            case R.id.menu_setting:
                break;
            case R.id.menu_about:
                AboutActivity.launch(MainActivity.this);
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

            //-主页
            mFragmentSparseArray.put(R.id.menu_todo, MainTabsFragment.newInstance());

            //weather
            mFragmentSparseArray.put(R.id.menu_more_service, OtherServerFragment.newInstance());
        }
        setMainFragment(R.id.menu_todo, mFragmentSparseArray, savedInstanceState == null);
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
        EditNoteActivity.launch(MainActivity.this,null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_activity_main, menu);
        initShowModeMenuIcon(menu.findItem(R.id.menu_note_show_mode));
        return true;
    }

    private void initShowModeMenuIcon(MenuItem item) {
        Resources res = Utils.getApp().getResources();
        if (C.noteListShowMode == C.STYLE_LINEAR) {
            item.setIcon(res.getDrawable(R.drawable.ic_border_all_white_24dp));
        } else {
            item.setIcon(res.getDrawable(R.drawable.ic_format_list_bulleted_white_24dp));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_search:
                break;
            case R.id.menu_note_show_mode:
                break;
            default:
                break;
        }
        return true;
    }

    public void setFabVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        mFab.setVisibility(visibility);
    }

    private void updateWeather() {
        //使用RxPermissions（基于RxJava2） - CSDN博客
        //           http://blog.csdn.net/u013553529/article/details/68948971
        RxPermissions permissions = new RxPermissions(this);
        Disposable disposable = permissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        View headerView = mNavView.getHeaderView(0);
                        new WeatherExecutor(MainActivity.this, headerView).execute();
                    } else {
                        ToastUtils.showShort(getString(R.string.no_permission_location));
                    }
                });
        mDisposableList.add(disposable);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

}
