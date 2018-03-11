package com.yueyue.todolist.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.component.CrashHandler;

import org.litepal.LitePal;

/**
 * author : yueyue on 2018/3/2 00:11
 * desc   : App基类
 */

public class MainApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        initTools();
        initBugsHandle();


    }

    private void initTools() {
        //初始化AndroidUtilCode
        Utils.init(sContext);
        LitePal.initialize(sContext);
    }

    private void initBugsHandle() {
        CrashHandler.init(new CrashHandler(sContext));
//        Bugtags.start(BuildConfig.BugtagsKey, sContext, Bugtags.BTGInvocationEventBubble);
    }


    public static Context getAppContext() {
        return sContext;
    }

    public static String getAppCacheDir() {
        return sContext.getCacheDir().toString();
    }
}
