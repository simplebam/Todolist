package com.yueyue.todolist.modules.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.yueyue.todolist.component.NotificationHelper;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.modules.main.db.NoteDbHelper;

/**
 * author : yueyue on 2018/3/22 22:37
 * desc   :
 */

public class AutoUpdateService extends IntentService {

    public static void launch(Context context) {
        //启动后台更新服务
        Intent intent = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
    }


    private final String TAG = AutoUpdateService.class.getSimpleName();

    public AutoUpdateService() {
        super("AutoUpdateService");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        updateNote();
        awakeService();
    }

    private void updateNote() {
        int todayNormalCount = NoteDbHelper.getInstance().loadTodayNormalCount();
        int todayPrivacyCount = NoteDbHelper.getInstance().loadTodayPrivacyCount();
        int[] counts = {todayNormalCount, todayPrivacyCount};
        new Handler(Looper.getMainLooper()).post(()->{
            NotificationHelper.showNotification(AutoUpdateService.this,counts);
        });
    }

    /**
     * 使得AutoUpdateService每autoUpdate小时唤醒一下更新数据
     */
    private void awakeService() {
        //开启闹钟
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int autoUpdate = PreferencesManager.getInstance().getAutoUpdate();
        if (autoUpdate<1) {
            autoUpdate=1;
        }
        int anHour = autoUpdate * 60 * 60 * 1000; // 这是autoUpdate小时的毫秒数
        //闹钟启动的计划时刻
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);

        if (manager == null) {
            return;
        }
        //使用前先清空之前的意图,这种做法类似于List使用前清空数据
        manager.cancel(pi);
        //设置闹钟
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }
}
