package com.yueyue.todolist.common.utils;

import android.support.annotation.NonNull;

/**
 * author : yueyue on 2018/3/5 15:39
 * desc   :
 */

public class StringUtils {

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

}
