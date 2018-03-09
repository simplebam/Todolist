package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * author : yueyue on 2018/3/9 10:01
 * desc   :
 */

public class Util {

    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 匹配掉无关信息
     */

    public static String replaceInfo(String city) {
        if (TextUtils.isEmpty(city)) return "";
        city = city.replace("API没有", "");
        return city;
    }

    public static String safeText(String msg) {
        return TextUtils.isEmpty(msg) ? "" : msg;
    }

    /**
     * 匹配掉错误信息
     */
    public static String replaceCity(String city) {
        city = safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
        return city;
    }


//    /**
//     * 将返回的JSON数据解析成Weather实体类,使用了V5接口
//     */
//    public static Weather handleWeatherResponse(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//
//            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
//            String weatherContent = jsonArray.getJSONObject(0).toString();
//            return new Gson().fromJson(weatherContent, Weather.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
