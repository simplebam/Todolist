package com.yueyue.todolist.common;

import com.yueyue.todolist.BuildConfig;
import com.yueyue.todolist.base.BaseApplication;

import java.io.File;

/**
 * author : yueyue on 2018/3/7 21:35
 * desc   : Common constants for activities and fragments.
 */

public class Constants {

    public static final String NET_CACHE = BaseApplication.getAppCacheDir() + File.separator + "NetCache";
    public static final String FIR_API_TOKEN = BuildConfig.FirApiToken;
    public static final String FIR_API_APPID = BuildConfig.FirAppID;
    public static final String KEY = BuildConfig.HeWeatherKey;// 和风天气 key

    public static final String VIEW_POSITION = "view_position";
    public static final String POSITION = "position";
    public static final String TYPE = "type";
}
