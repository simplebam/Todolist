package com.yueyue.todolist.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.component.CachePlanTaskStore;
import com.yueyue.todolist.component.CrashHandler;

/**
 * author : yueyue on 2018/3/2 00:11
 * desc   : App基类
 */

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();
    public static Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;


        CrashHandler.init(new CrashHandler(this));
//        Bugtags.start(BuildConfig.BugtagsKey, this, Bugtags.BTGInvocationEventBubble);

        //初始化AndroidUtilCode
        Utils.init(this);
        ForegroundObserver.init(this);

        CachePlanTaskStore.initialize(this);
    }


    public static Context getContext() {
        return sContext;
    }
}
