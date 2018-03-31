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

    private static final java.lang.String DEFUALT_DATE_FORMAT = DateStyle.YYYY_MM_DD_HH_MM_SS.getValue();

    private DateUtils() {
    }

    public static enum DateStyle {
        MM_DD("MM-dd"),
        YYYY_MM("yyyy-MM"),
        YYYY_MM_DD("yyyy-MM-dd"),
        MM_DD_HH_MM("MM-dd HH:mm"),
        MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

        MM_DD_EN("MM/dd"),
        YYYY_MM_EN("yyyy/MM"),
        YYYY_MM_DD_EN("yyyy/MM/dd"),
        MM_DD_HH_MM_EN("MM/dd HH:mm"),
        MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),

        MM_DD_CN("MM月dd日"),
        MM_CN("MM月"),
        YYYY_MM_CN("yyyy年MM月"),
        YYYY_MM_DD_CN("yyyy年MM月dd日"),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm"),
        MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
        YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),

        HH_MM("HH:mm"),
        HH_MM_SS("HH:mm:ss");


        private String value;

        DateStyle(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum Week {
        MONDAY("星期一", "Monday", "Mon.", 1),
        TUESDAY("星期二", "Tuesday", "Tues.", 2),
        WEDNESDAY("星期三", "Wednesday", "Wed.", 3),
        THURSDAY("星期四", "Thursday", "Thur.", 4),
        FRIDAY("星期五", "Friday", "Fri.", 5),
        SATURDAY("星期六", "Saturday", "Sat.", 6),
        SUNDAY("星期日", "Sunday", "Sun.", 7);

        String name_cn;
        String name_en;
        String name_enShort;
        int number;

        Week(String name_cn, String name_en, String name_enShort, int number) {
            this.name_cn = name_cn;
            this.name_en = name_en;
            this.name_enShort = name_enShort;
            this.number = number;
        }

        public String getChineseName() {
            return name_cn;
        }

        public String getName() {
            return name_en;
        }

        public String getShortName() {
            return name_enShort;
        }

        public int getNumber() {
            return number;
        }
    }


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
        return getDistanceDays(date, new Date());
    }

    /**
     * 获取所给日期与现在的相差天数
     *
     * @return 相差天数
     */
    public static long getDistanceDaysToNow(long time) {
        return getDistanceDays(new Date(time), new Date());
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

    public static Date string2Date(String format, String date) {
        try {
            DateFormat newFormat = new SimpleDateFormat(format);
            return newFormat.parse(date);
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

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }


    /**
     * 两个日期相差的年
     *
     * @param millis  第一个时间戳
     * @param millis1 第二个时间戳
     * @describe
     */
    public static int getIntervalYears(long millis, long millis1) {
        Date date1 = new Date(millis);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Date date2 = new Date(millis1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
    }

    /**
     * 判断日期是否今年的
     *
     * @param millis 第一个时间戳
     * @describe
     */
    public static boolean isSameYear(long millis) {
        return getIntervalYears(millis, Calendar.getInstance().getTimeInMillis()) == 0;
    }

    /**
     * 判断是否是同一月
     *
     * @param millis  第一个时间戳
     * @param millis1 第二个时间戳
     * @describe
     */
    public static boolean isSameMonth(long millis, long millis1) {
        Date date1 = new Date(millis);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Date date2 = new Date(millis1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }


    /**
     * 判断这个日期是否这月
     * @param millis  第一个时间戳
     * @describe
     */
    public static boolean isSameMonth(long millis) {
        return isSameMonth(millis,new Date().getTime());
    }

}
