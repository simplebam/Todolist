package com.yueyue.todolist.common;

import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.BuildConfig;
import com.yueyue.todolist.common.utils.MyFileUtils;

/**
 * author : yueyue on 2018/3/7 21:35
 * desc   : Common constants for activities and fragments.
 */

public class C {

    //public static final String NET_CACHE = MainApplication.getAppCacheDir() + File.separator + "NetCache";
    public static final String NET_CACHE = MyFileUtils.getDiskCacheDirFileStr(Utils.getApp(),"NetCache");


    public static int ONE_HOUR = 1000 * 60 * 60;//60分钟

    //Alibaba转账
    public static final String ALI_PAY = BuildConfig.ALiPayKey;

    // fir 检查升级版本使用
    public static final String FIR_API_TOKEN = BuildConfig.FirApiToken;
    public static final String FIR_API_APPID = BuildConfig.FirAppID;
    // 和风天气 key
    public static final String HE_WEATHER_KEY = BuildConfig.HeWeatherKey;

    // Mob天气 key
    public static final String MOB_APP_KEY = BuildConfig.MobAppKey;


    // note的RecyclerView的显示布局样式
    public static final int STYLE_LINEAR = 1; // 线性布局
    public static final int STYLE_GRID = 2; // 网格布局

    // 图片标记的前便签和后标签
    public static String imageTabBefore="<image>";
    public static String imageTabAfter="</image>";




    public static String AVATARS_PERSONAL_URL ="https://avatars1.githubusercontent.com/u/25021641?s=460&v=4";





}
