package com.yueyue.todolist.modules.main.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.modules.details.ui.PlanTaskActivity;
import com.yueyue.todolist.modules.main.impl.OnGuideChangedListenerImpl;

import java.util.Date;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_GUIDE_BUILD = "guide_build";
    private static final String KEY_GUIDE_SIDE = "guide_side";

    public static final long DRAWER_CLOSE_DELAY = 230L;
    private boolean backPressed = false;
    private SparseArray<Fragment> mFragmentSparseArray;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    //---------- 自定义那里的ToolBar---------
    @BindView(R.id.toolbar_home_as_up)
    ImageView mHomeAsUp;

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_sub_title_year)
    TextView mToolbarSubTitleYear;
    @BindView(R.id.toolbar_sub_title_day)
    TextView mToolbarSubTitleDay;

    @BindView(R.id.toolbar_to_today)
    TextView mToolbarToToday;


    @BindView(R.id.fab)
    FloatingActionButton mFab;


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
        setupView();


        // FIXME: 2018/3/4 仿照就看天气或者DonateGrils
        //Update();
        // FIXME: 2018/3/4 接上天气/仿照PonyMusic
        //updateWeather();
        initFragments(savedInstanceState);
    }


    private void setupView() {
        //隐藏Toolbar
        hideToolbar();

        //初始化statusView
        View statusView = findViewById(R.id.view_status);
        ViewGroup.LayoutParams layoutParams = statusView.getLayoutParams();
        layoutParams.height = BarUtils.getStatusBarHeight();
        statusView.setLayoutParams(layoutParams);

        initNavigationView();
        initListener();

    }

    private void initListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                showGuideBuild();
            }
        });

        mFab.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, PlanTaskActivity.class);
//            intent.putExtra(PlanTaskActivity.KEY_SHOW_TYPE, PlanTaskActivity.TYPE_NEW_BUILD);
//            if (mLastSelectedSideId == ID_TOMORROW) {
//                intent.putExtra(PlanTaskActivity.KEY_IS_TOMORROW, true);
//            }
//            startActivity(intent);
        });

        mHomeAsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
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
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_today:
                PlanTaskActivity.launch(this, PlanTaskActivity.TYPE_NEW_BUILD, null, false);
                break;
            case R.id.menu_tomorrow:
                break;
            case R.id.menu_calendar:
                break;
            case R.id.menu_all:
                break;
            case R.id.menu_finished:
                break;
            case R.id.menu_one_city:
                break;
            case R.id.menu_multi_cities:
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
//        if (mFragmentSparseArray == null) {
//            String[] titles, types;
//            mFragmentSparseArray = new SparseArray<>();
//            String[] all = getResources().getStringArray(R.array.db_titles);
//
////            secretMode = true;
//            Log.d(TAG, "initFragments: secret " + secretMode + " " + SpUtil.getString("deviceId"));
//            if (secretMode) {
//                //Gank & Douban
//                titles = all;
//                types = new String[]{TYPE_GANK, TYPE_DB_RANK, TYPE_DB_BREAST, TYPE_DB_BUTT, TYPE_DB_LEG, TYPE_DB_SILK};
//            } else {
//                titles = new String[]{all[0]};
//                types = new String[]{TYPE_GANK};
//            }
//            mFragmentSparseArray.put(R.taskId.nav_beauty, MainTabsFragment.newInstance(titles, types));
//
//            //二次元
//            titles = getResources().getStringArray(R.array.a_titles);
//            types = new String[]{TYPE_A_ANIME, TYPE_A_FULI, TYPE_A_HENTAI, TYPE_A_UNIFORM, TYPE_A_ZATU};
//            mFragmentSparseArray.put(R.taskId.nav_a, MainTabsFragment.newInstance(titles, types));
//            //favorite
//            mFragmentSparseArray.put(R.taskId.nav_favorite, new FavoriteFragment());
//        }
//        setMainFragment(R.taskId.nav_beauty, mFragmentSparseArray, savedInstanceState == null);
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

    private void showGuideSide() {
        Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
                    @Override
                    public void onRemoved(Controller controller) {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                })
                .setBackgroundColor(getResources().getColor(R.color.guide_bg_color))
                .setEveryWhereCancelable(true)
                .setLayoutRes(R.layout.guide_side_view_layout)
                .alwaysShow(false)
                .addHighLight(mHomeAsUp, HighLight.Type.CIRCLE)
                .setLabel(KEY_GUIDE_SIDE)
                .build();
        controller.show();
    }


    private void showGuideBuild() {
        Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
                    @Override
                    public void onRemoved(Controller controller) {
//                        Intent intent = new Intent(MainActivity.this, PlanTaskActivity.class);
//                        intent.putExtra(PlanTaskActivity.KEY_SHOW_TYPE, PlanTaskActivity.TYPE_NEW_BUILD);
//                        startActivity(intent);
                    }
                })
                .setBackgroundColor(getResources().getColor(R.color.guide_bg_color))
                .setEveryWhereCancelable(true)
                .setLayoutRes(R.layout.guide_build_view_layout)
                .alwaysShow(false)
                .addHighLight(mFab, HighLight.Type.CIRCLE)
                .setLabel(KEY_GUIDE_BUILD)
                .build();
        controller.show();
    }


}
