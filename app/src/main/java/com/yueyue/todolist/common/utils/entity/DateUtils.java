package com.yueyue.todolist.common.utils.entity;

import java.util.Date;

/**
 * author : yueyue on 2018/3/5 15:27
 * desc   : 时间处理类
 * quote :   Date类学习总结(Calendar Date 字符串 相互转换 格式化) - CSDN博客
 *                http://blog.csdn.net/footballclub/article/details/45191061
 */

public class DateUtils {

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






}
