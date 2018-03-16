package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;

/**
 * author : yueyue on 2018/3/14 10:10
 * desc   :
 */

public class EnvUtil {

    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return TypedValue.complexToDimensionPixelSize(tv.data, displayMetrics);
        }
        return ConvertUtils.dp2px(44);
    }

    public static int getStatusBarHeight() {
        Resources res = Utils.getApp().getResources();
        if (sStatusBarHeight == 0) {
            int resourceId = res.getIdentifier("status_bar_height",
                    "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }
}