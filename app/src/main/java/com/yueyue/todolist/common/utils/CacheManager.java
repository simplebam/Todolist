package com.yueyue.todolist.common.utils;

import android.app.Notification;

/**
 * author : yueyue on 2018/3/4 09:51
 * desc   :
 */

public class CacheManager {

    private static final String WEATHER_CITY = "weather_city";
    private static final String WEATHER_AUTO_UPDATE = "weather_update_time"; //天气自动更新时长
    private static final String NOTIFICATION_MODEL = "notification_model";//通知栏常驻

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return SingtonHolder.sInstance;
    }

    private static class SingtonHolder {
        private static final CacheManager sInstance = new CacheManager();
    }


    //----------Weather方面---------------
    /**
     *   天气城市
     */
    public String getCityName() {
        return PreferencesUtil.getString(WEATHER_CITY, null);
    }

    public void saveCityName(String value) {
        PreferencesUtil.saveString(WEATHER_CITY, value);
    }


    /**
     * 天气自动更新时间 hours
     */
    public void saveAutoUpdate(int t) {
        PreferencesUtil.saveInt(WEATHER_AUTO_UPDATE, t);
    }

    public int getAutoUpdate() {
        return PreferencesUtil.getInt(WEATHER_AUTO_UPDATE, 3);
    }



    /**
     * 通知栏模式 默认为常驻
     */
    public void saveNotificationModel(int t) {
        PreferencesUtil.saveInt(NOTIFICATION_MODEL, t);
    }

    public int getNotificationModel() {
        return PreferencesUtil.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
    }


    //----便签方面-----------
}
