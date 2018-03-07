package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yueyue.todolist.R;

/**
 * author : yueyue on 2018/3/2 00:54
 * desc   :
 */

public class VersionUtil {

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getString(R.string.can_not_find_version_name);
    }
}
