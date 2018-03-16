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
        //一定要先初始化AndroidUtilCode,因为很多类初始化都用到Utils.getApp()获得全局的Context
        Utils.init(sContext);
        initTools();
        initBugsHandle();


    }

    private void initTools() {
        LitePal.initialize(sContext);
    }

    private void initBugsHandle() {
        CrashHandler.init(new CrashHandler(sContext));
//        Bugtags.start(BuildConfig.BugtagsKey, sContext, Bugtags.BTGInvocationEventBubble);
    }
}
