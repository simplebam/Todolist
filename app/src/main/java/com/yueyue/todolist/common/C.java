package com.yueyue.todolist.common;

import com.yueyue.todolist.BuildConfig;
import com.yueyue.todolist.base.BaseApplication;

import java.io.File;

/**
 * author : yueyue on 2018/3/9 09:59
 * desc   :
 */

public class C {

    public static final String FIR_API_TOKEN = BuildConfig.FirApiToken;
    public static final String FIR_APP_ID = BuildConfig.FirAppID;
    public static final String KEY = BuildConfig.WeatherKey;// 和风天气 key

    public static final String ALI_PAY = BuildConfig.ALiPayKey;
    public static final String ALI_PAY1 = "FKX07563WDYUZJTAW5W029";
    public static final String ALI_PAY_HONGBAO = "https://qr.alipay.com/" + ALI_PAY;

    public static final String MULTI_CHECK = "multi_check";

    public static final String ORM_NAME = "cities.db";

    public static final String UNKNOWN_CITY = "unknown city";

    public static final String NET_CACHE = BaseApplication.getAppCacheDir() + File.separator + "NetCache";

    public static final String DANTE = "dante";
}
