package com.yueyue.todolist.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
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

        //初始化AndroidUtilCode
        Utils.init(this);

        ForegroundObserver.init(this);

//        String processName = MyProcessUtil.getProcessName(this);
//        Log.i(TAG, "onCreate processName : " + processName);

        // FIXME: 2018/3/2 注释了CachePlanTaskStore跟SkinManager
//        CachePlanTaskStore.initialize(this);
//        SkinManager.getInstance().init(this);
    }


    public static Context getContext() {
        return sContext;
    }
}
