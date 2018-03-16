package com.yueyue.todolist.component;

import android.app.Notification;

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
    public static final String CURRENT_FOLDER = "current_folder";
    public static final String LOCK_PASSWORD = "lock_password";
    public static final String IS_USE_RECYCLE = "is_use_recycle";
    public static final String IS_LOCKED = "is_locked";

    //OtherServerFragment
    public static final String OTHER_SERVER_FRAGMENT_IS_GRID = "other_server_fragment_is_grid";

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
        return PreferencesUtil.getString(VERSION_NAME,"");
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
     * 通知栏模式 默认为常驻
     */
    public void saveNotificationModel(int t) {
        PreferencesUtil.saveInt(NOTIFICATION_MODEL, t);
    }

    public int getNotificationModel() {
        return PreferencesUtil.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
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

    // OtherServerFragment

    /**
     * 第一次进入应用
     */
    public boolean getOtherServerFragmentIsGrid(boolean defValue) {
        return PreferencesUtil.getBoolean(OTHER_SERVER_FRAGMENT_IS_GRID, defValue);
    }

    public void saveOtherServerFragmentIsGrid(boolean value) {
        PreferencesUtil.saveBoolean(OTHER_SERVER_FRAGMENT_IS_GRID, value);
    }

}
