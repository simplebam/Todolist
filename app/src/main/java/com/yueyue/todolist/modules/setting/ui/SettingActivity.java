package com.yueyue.todolist.modules.setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;

/**
 * author : yueyue on 2018/3/22 21:24
 * desc   :
 */

public class SettingActivity extends BaseActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getString(R.string.setting));
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new SettingFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        }
        return true;
    }
}
