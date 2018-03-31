package com.yueyue.todolist.component;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yueyue.todolist.R;
import com.yueyue.todolist.modules.main.ui.MainActivity;

/**
 * author : yueyue on 2018/3/9 13:13
 * desc   :
 */

public class NotificationHelper {
    private static final int NOTIFICATION_ID = 233;

    public static void showNotification(Context context, @NonNull int[] counts) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.today_plan))
                .setContentText(String.format("Todo:%s 私密: %s ", counts[0], counts[1]))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        boolean b = PreferencesManager.getInstance().getNotificationResident();
        notification.flags = b ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }
    }

    public static void cancelWeatherNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(NOTIFICATION_ID);
        }
    }
}
