package com.yueyue.todolist.modules.main.component;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yueyue.todolist.R;
import com.yueyue.todolist.common.listener.IExecutor;
import com.yueyue.todolist.common.utils.CacheManager;
import com.yueyue.todolist.common.utils.Util;
import com.yueyue.todolist.component.AMapLocationer;
import com.yueyue.todolist.component.RetrofitSingleton;
import com.yueyue.todolist.modules.main.domain.MobWeather;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * author : yueyue on 2018/3/10 13:19
 * desc   : 参考 PonyMusic 做法 https://www.jianshu.com/p/1c0f5c4f64fa
 */

public class WeatherExecutor implements IExecutor {

    private static final String TAG = WeatherExecutor.class.getSimpleName();

    private RxAppCompatActivity mActivity;
    @BindView(R.id.ll_weather)
    LinearLayout llWeather;
    @BindView(R.id.iv_weather_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_weather_temp)
    TextView tvTemp;
    @BindView(R.id.tv_weather_city)
    TextView tvCity;
    @BindView(R.id.tv_weather_wind)
    TextView tvWind;


    public WeatherExecutor(RxAppCompatActivity activity, View navigatioHeaderView) {
        mActivity = activity;
        ButterKnife.bind(this, navigatioHeaderView);

    }


    @Override
    public void execute() {
        location();
    }

    private void location() {
        AMapLocationer.getInstance(mActivity, CacheManager.getInstance().getAutoUpdate())
                .location(aMapLocation -> {
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            //定位成功。
                            aMapLocation.getLocationType();
                            CacheManager.getInstance().saveCityName(Util.replaceCity(aMapLocation.getCity()));
                            ToastUtils.showShort("定位成功");
                        }
                        load();
                    }
                });
    }

    private void load() {
        String cityName = CacheManager.getInstance().getCityName();
        if (TextUtils.isEmpty(cityName)) {
            cityName = "广州";
            CacheManager.getInstance().saveCityName(cityName);
            ToastUtils.showShort("定位失败,自动查询广州天气");
        }

        fetchDataByNetWork(cityName)
                .doOnError(throwable -> CacheManager.getInstance().saveCityName("广州"))
                .doOnNext(weather -> updateView(weather))
                .doOnComplete(() -> ToastUtils.showShort(mActivity.getString(R.string.update_complete)))
                .subscribe();

    }

    private void updateView(MobWeather weather) {
        if (weather != null) {
            llWeather.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(getWeatherIcon(weather));
            tvTemp.setText(weather.temperature);
            tvCity.setText(weather.city);
            StringBuilder sb = new StringBuilder(weather.wind)
                    .append(" ").append(weather.humidity);
            tvWind.setText(sb.toString());
        }
    }


    /**
     * "多云,少云,晴,阴,小雨,雨,雷阵雨,中雨,阵雨,零散阵雨,零散雷雨,小雪,雨夹雪,阵雪,霾",
     */
    private int getWeatherIcon(MobWeather mobWeather) {
        if (mobWeather == null) {
            return R.drawable.ic_weather_sunny;
        }

        String weatherStr = mobWeather.weather;
        if (weatherStr.contains("-")) {
            weatherStr = weatherStr.substring(0, weatherStr.indexOf("-"));
        }
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int resId;
        if (weatherStr.contains("晴")) {
            if (hour >= 7 && hour < 19) {
                resId = R.drawable.ic_weather_sunny;
            } else {
                resId = R.drawable.ic_weather_sunny_night;
            }
        } else if (weatherStr.contains("少云")) {
            if (hour >= 7 && hour < 19) {
                resId = R.drawable.ic_weather_cloudy;
            } else {
                resId = R.drawable.ic_weather_cloudy_night;
            }
        } else if (weatherStr.contains("阴")) {
            resId = R.drawable.ic_weather_overcast;
        } else if (weatherStr.contains("雷阵雨")) {
            resId = R.drawable.ic_weather_thunderstorm;
        } else if (weatherStr.contains("雨夹雪")) {
            resId = R.drawable.ic_weather_sleet;
        } else if (weatherStr.contains("雨")) {
            resId = R.drawable.ic_weather_rain;
        } else if (weatherStr.contains("雪")) {
            resId = R.drawable.ic_weather_snow;
        } else if (weatherStr.contains("雾") || weatherStr.contains("霾")) {
            resId = R.drawable.ic_weather_foggy;
        } else if (weatherStr.contains("风") || weatherStr.contains("飑")) {
            resId = R.drawable.ic_weather_typhoon;
        } else if (weatherStr.contains("沙") || weatherStr.contains("尘")) {
            resId = R.drawable.ic_weather_sandstorm;
        } else {
            resId = R.drawable.ic_weather_cloudy;
        }
        return resId;
    }


    private void release() {
        mActivity = null;
        llWeather = null;
        ivIcon = null;
        tvTemp = null;
        tvCity = null;
        tvWind = null;
    }


    /**
     * 从网络获取
     */
    private Observable<MobWeather> fetchDataByNetWork(String cityName) {
        return RetrofitSingleton.getInstance()
                .fetchMobWeather(cityName)
                .compose(mActivity.bindToLifecycle());
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onExecuteSuccess(Object o) {

    }

    @Override
    public void onExecuteFail(Exception e) {

    }
}
