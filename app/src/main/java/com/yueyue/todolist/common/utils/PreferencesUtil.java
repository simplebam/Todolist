package com.yueyue.todolist.common.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.Utils;

/**
 * author : yueyue on 2018/3/11 10:16
 * desc   : SharedPreferences 工具类
 */

public class PreferencesUtil {
    private static SharedPreferences mShare = PreferenceManager.getDefaultSharedPreferences(Utils.getApp());
    private static SharedPreferences.Editor mEdit = mShare.edit();

    public static void saveString(String key, String value) {
        mEdit.putString(key, value);
        mEdit.commit();
    }

    public static void saveInt(String key, int value) {
        mEdit.putInt(key, value);
        mEdit.commit();
    }

    public static void saveBoolean(String key, boolean value) {
        mEdit.putBoolean(key, value);
        mEdit.commit();
    }


    public static String getString(String key, String value) {
        return mShare.getString(key, value);
    }

    public static boolean getBoolean(String key, boolean defalut) {
        return mShare.getBoolean(key, defalut);
    }

    public static int getInt(String key, int defalut) {
        return mShare.getInt(key, defalut);
    }
}
