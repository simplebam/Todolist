package com.yueyue.todolist.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author : yueyue on 2018/3/5 15:27
 * desc   : 时间处理类
 * quote :   Date类学习总结(Calendar Date 字符串 相互转换 格式化) - CSDN博客
 * http://blog.csdn.net/footballclub/article/details/45191061
 */

public class DateUtils {

    public static String DEFUALT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //作为存储
    public static String DEFUALT_DATE_FORMAT2 = "yyyy-MM-dd";

    public static String DEFUALT_DATE_FORMAT3 = "MM-dd";

    public static String DEFUALT_DATE_FORMAT4 = "HH:mm";

    /**
     * 两个时间之间相差距离多少秒
     */
    public static long getDistanceSecond(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000;
    }


    /**
     * 两个时间之间相差距离多少分钟
     */
    public static long getDistanceMinutes(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60);
    }


    /**
     * 两个时间之间相差距离多少小时
     */
    public static long getDistanceHours(Date date1, Date date2) {
        return getDistanceHours(date1.getTime(), date2.getTime());
    }

    /**
     * 两个日期相差的天数
     */
    public static long getDistanceDays(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
    }


    /**
     * 两个时间之间相差距离多少小时
     */
    public static long getDistanceHours(long time1, long time2) {
        return (time1 - time2) / (1000 * 60 * 60);
    }


    /**
     * 获取所给日期与现在的相差天数
     *
     * @return 相差天数
     */
    public static long getDistanceDaysToNow(Date date) {
        return getDistanceDays(new Date(), date);
    }


    public static String date2String(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        return formater.format(date);
    }

    /**
     * 格式化日期字符串
     */
    public static String date2String(String format, Date date) {
        try {
            DateFormat newFormat = new SimpleDateFormat(format);
            return newFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String getBeforeDate(int days) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        //如果是后退几天，就写 -天数 例如：
        rightNow.add(Calendar.DAY_OF_MONTH, -days);
        //进行时间转换
        return sim.format(rightNow.getTime());
    }

    public static String getAfterDate(int days) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        //得到当前时间，+3天
        rightNow.add(Calendar.DAY_OF_MONTH, days);
        return sim.format(rightNow.getTime());
    }
}
