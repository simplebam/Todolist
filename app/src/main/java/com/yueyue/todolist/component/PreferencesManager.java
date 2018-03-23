package com.yueyue.todolist.component;

import com.yueyue.todolist.common.utils.PreferencesUtil;

/**
 * author : yueyue on 2018/3/4 09:51
 * desc   :
 */

public class PreferencesManager {

    private static final String IS_FIRST = "is_first";


    private static final String VERSION_NAME = "version_name";

    private static final String WEATHER_CITY = "weather_city";
    private static final String WEATHER_AUTO_UPDATE = "weather_update_time"; //天气自动更新时长
    private static final String NOTIFICATION_MODEL = "notification_model";//通知栏常驻


    private static final String NOTE_LIST_SHOW_MODE = "note_list_show_mode";
    private static final String LOCK_PASSWORD = "lock_password";
    private static final String IS_AUTO_CHECK_VERSION = "is_auto_check_version";


    private PreferencesManager() {
    }

    public static PreferencesManager getInstance() {
        return SingtonHolder.sInstance;
    }

    private static class SingtonHolder {
        private static final PreferencesManager sInstance = new PreferencesManager();
    }

    /**
     * 应用版本号
     */
    public String getVersioName() {
        return PreferencesUtil.getString(VERSION_NAME, "");
    }

    public void saveVersioName(String value) {
        PreferencesUtil.saveString(VERSION_NAME, value);
    }

    /**
     * 第一次进入应用
     */
    public boolean getIsFirst() {
        return PreferencesUtil.getBoolean(IS_FIRST, true);
    }

    public void saveIsFirst(boolean value) {
        PreferencesUtil.saveBoolean(IS_FIRST, value);
    }

    //----------Weather方面---------------

    /**
     * 天气城市
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
     * 通知栏模式 默认不常驻
     */
    public void saveNotificationResident(boolean value) {
        PreferencesUtil.saveBoolean(NOTIFICATION_MODEL, value);
    }

    public boolean getNotificationResident() {
        return PreferencesUtil.getBoolean(NOTIFICATION_MODEL,false);
    }

    //----便签方面-----------


    /**
     * 便签列表显示模式
     */
    public void saveNoteListShowMode(int showMode) {
        PreferencesUtil.saveInt(NOTE_LIST_SHOW_MODE, showMode);
    }

    public int getNoteListShowMode(int defShowMode) {
        return PreferencesUtil.getInt(NOTE_LIST_SHOW_MODE, defShowMode);
    }


    /**
     * 是否自动更新
     */
    public boolean getIsAutoCheckVersion(boolean defValue) {
        return PreferencesUtil.getBoolean(IS_AUTO_CHECK_VERSION, defValue);
    }

    public void saveIsAutoCheckVersion(boolean value) {
        PreferencesUtil.saveBoolean(IS_AUTO_CHECK_VERSION, value);
    }

    /**
     * lock密码
     */
    public String getLockPassword(String defValue) {
        return PreferencesUtil.getString(LOCK_PASSWORD, defValue);
    }

    public void saveLockPassword(String password) {
        PreferencesUtil.saveString(LOCK_PASSWORD, password);
    }

}
