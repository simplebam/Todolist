package com.yueyue.testapp;

import android.app.Application;

import org.litepal.LitePal;

/**
 * author : yueyue on 2018/3/5 21:12
 * desc   :
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
