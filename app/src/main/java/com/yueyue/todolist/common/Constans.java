package com.yueyue.todolist.common;

import com.yueyue.todolist.BuildConfig;
import com.yueyue.todolist.base.MainApplication;

import java.io.File;

/**
 * author : yueyue on 2018/3/7 21:35
 * desc   : Common constants for activities and fragments.
 */

public class Constans {

    public static final String NET_CACHE = MainApplication.getAppCacheDir() + File.separator + "NetCache";

    public static int ONE_HOUR = 1000 * 60 * 60;//60分钟

    // fir 检查升级版本使用
    public static final String FIR_API_TOKEN = BuildConfig.FirApiToken;
    public static final String FIR_API_APPID = BuildConfig.FirAppID;
    // 和风天气 key
    public static final String HE_WEATHER_KEY = BuildConfig.HeWeatherKey;

    // Mob天气 key
    public static final String MOB_APP_KEY = BuildConfig.MobAppKey;


}
