package com.yueyue.todolist.common.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.yueyue.todolist.component.PLog;

/**
 * author : yueyue on 2018/3/2 00:33
 * desc   : 进程操作的工具类
 */

public class MyProcessUtil {

    private static final String TAG = MyProcessUtil.class.getSimpleName();

    public static String getProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) return appProcess.processName;
            }
        } catch (Exception ex) {
            PLog.d(TAG, "getProcessName, ex: " + ex.toString());
        }
        return "";
    }
}
