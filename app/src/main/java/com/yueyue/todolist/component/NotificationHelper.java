package com.yueyue.todolist.component;

/**
 * author : yueyue on 2018/3/9 13:13
 * desc   :
 */

public class NotificationHelper {
    private static final int NOTIFICATION_ID = 233;

//    public static void showWeatherNotification(Context context, @NonNull Weather weather) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder = new Notification.Builder(context);
//        Notification notification = builder.setContentIntent(pendingIntent)
//                .setContentTitle(weather.basic.city)
//                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
//                .setSmallIcon(SpUtil.getInstance().getInt(weather.now.cond.txt, R.drawable.weather_none))
//                .build();
//        notification.flags = SpUtil.getInstance().getNotificationModel();
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(NOTIFICATION_ID, notification);
//    }
//
//    public static void cancelWeatherNotification(Context context) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (manager != null) {
//            manager.cancel(NOTIFICATION_ID);
//        }
//    }
}
