package com.yueyue.todolist.common.utils;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yueyue.todolist.base.BaseApplication;

/**
 * author : yueyue on 2018/3/4 09:51
 * desc   :
 */

public class SpUtil {
    private SharedPreferences mPrefs;
    private String FILE_NAME = "appConfig";

    private static final String AVATAR_KEY = "avatar_key";
    public static final String WEATHER_DATA = "weather_data";
    public static final String KEY_WEATHER_CITY = "weather_city";
    public static final String WEATHER_ANIM_START = "weather_animation_start";//首页item动画
    public static final String WEATHER_AUTO_UPDATE = "weather_update_time"; //自动更新时长
    public static final String NOTIFICATION_MODEL = "notification_model";//通知栏常驻


    public static int ONE_HOUR = 1000 * 60 * 60;//60分钟

    private SpUtil() {
        mPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SpUtil getInstance() {
        return SingtonHolder.sInstance;
    }


    private static class SingtonHolder {
        private static final SpUtil sInstance = new SpUtil();
    }

    public void putString(String key, String value) {
        mPrefs.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public void setBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    public void putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }



    public void setAvatar(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        putString(AVATAR_KEY, value);
    }

    public String getAvatar() {
        return getString(AVATAR_KEY, "");
    }


    public String getCityName() {
        return getString(KEY_WEATHER_CITY, null);
    }

    public void putCityName(String value) {
        putString(KEY_WEATHER_CITY, value);
    }

    // 首页 Item 动画效果 默认关闭

    public void putWeatherAnim(boolean b) {
        mPrefs.edit().putBoolean(WEATHER_ANIM_START, b).commit();
    }

    public boolean getWeatherAnim() {
        return mPrefs.getBoolean(WEATHER_ANIM_START, false);
    }


    //  通知栏模式 默认为常驻
    public void putNotificationModel(int t) {
        mPrefs.edit().putInt(NOTIFICATION_MODEL, t).commit();
    }

    public int getNotificationModel() {
        return mPrefs.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
    }

    // 自动更新时间 hours
    public void setAutoUpdate(int t) {
        mPrefs.edit().putInt(WEATHER_AUTO_UPDATE, t).commit();
    }

    public int getAutoUpdate() {
        return mPrefs.getInt(WEATHER_AUTO_UPDATE, 3);
    }

}
