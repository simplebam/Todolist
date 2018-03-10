package com.yueyue.todolist.component;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * author : yueyue on 2018/3/10 14:00
 * desc   :
 * quote  : 入门指南-Android 定位SDK | 高德地图API
 *                 https://lbs.amap.com/api/android-location-sdk/gettingstarted)
 */

public class AMapLocationer {
    private static int ONE_HOUR = 1000 * 60 * 60;//60分钟

    private AMapLocationClient mLocationClient;
    private int mAutoUpdateTime = 1;

    private static AMapLocationer sIntance;


    private AMapLocationer(Context context, int autoUpdateTime) {
        mAutoUpdateTime = autoUpdateTime;
        initClient(context);
    }


    public static AMapLocationer getInstance(Context context, int autoUpdateTime) {
        if (context == null) {
            throw new RuntimeException("AMapLocationer 创建时候 context=null");
        }
        return new AMapLocationer(context, autoUpdateTime);
    }

    private void initClient(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationOption(initOption(context));
    }

    private AMapLocationClientOption initOption(Context context) {
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationOption.setNeedAddress(true);
        locationOption.setOnceLocation(true);
        //设置定位间隔 单位毫秒
        locationOption.setInterval((mAutoUpdateTime == 0 ? 100 : mAutoUpdateTime) * ONE_HOUR);
        return locationOption;
    }


    /**
     * 高德定位
     */
    public void location(AMapLocationListener listener) {
        mLocationClient.setLocationListener(aMapLocation -> {
            if (listener != null) listener.onLocationChanged(aMapLocation);
        });

        mLocationClient.startLocation();
    }

}
