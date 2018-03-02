package com.yueyue.todolist.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author : yueyue on 2018/3/4 09:51
 * desc   :
 */

public class BasePreference {
    private SharedPreferences sp;
    private String FILE_NAME = "appConfig";

    protected BasePreference() {
        sp = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    protected void setString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    protected String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    protected void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    protected void setInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    protected int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }


}
