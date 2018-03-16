package com.yueyue.todolist.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * author : yueyue on 2018/3/14 10:09
 * desc   :
 */

public class StatusBarUtil {

    public static void setImmersiveStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT > 21) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT == 19) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void setImmersiveStatusBarToolbar(@NonNull Toolbar toolbar, Context context) {
        ViewGroup.MarginLayoutParams toolLayoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        toolLayoutParams.height = EnvUtil.getStatusBarHeight() + EnvUtil.getActionBarSize(context);
        toolbar.setLayoutParams(toolLayoutParams);
        toolbar.setPadding(0, EnvUtil.getStatusBarHeight(), 0, 0);
        toolbar.requestLayout();
    }
}