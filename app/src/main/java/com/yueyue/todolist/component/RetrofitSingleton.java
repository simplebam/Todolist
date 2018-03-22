package com.yueyue.todolist.component;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.common.C;
import com.yueyue.todolist.common.utils.Util;
import com.yueyue.todolist.modules.about.domain.Version;
import com.yueyue.todolist.modules.main.domain.MobWeather;
import com.yueyue.todolist.modules.weather.domain.Weather;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static ApiInterface sApiService = null;
    private static Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttpClient = null;

    private void init() {
        initOkHttp();
        initRetrofit();
        sApiService = sRetrofit.create(ApiInterface.class);
    }

    private RetrofitSingleton() {
        init();
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(C.NET_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!Util.isNetworkConnected(Utils.getApp())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            if (Util.isNetworkConnected(Utils.getApp())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return newBuilder.build();
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        sOkHttpClient = builder.build();
    }

    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.HEWEATHER_HOST)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())  //设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //RxJava 适配器
                .build();
    }


    private static Consumer<Throwable> disposeFailureInfo(Throwable t) {
        return throwable -> {
            if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                    t.toString().contains("UnknownHostException")) {
                ToastUtils.showShort("网络问题");
            } else if (t.toString().contains("API没有")) {
                ToastUtils.showShort("错误: " + t.getMessage());
            } else {
                ToastUtils.showShort("错误: " + t.getMessage());
            }
            PLog.w(t.getMessage());
        };
    }


    /**
     * 和风status状态码:  "no more requests"--->/(ㄒoㄒ)/~~,API免费次数已用完
     * 和风status状态码:  "unknown city"--->API没有这个城市
     * 和风status状态码:  "param invalid"--->,API没有缺少查询的参数
     */
    public Observable<Weather> fetchWeather(String city) {
        return sApiService.mWeatherAPI(city, C.HE_WEATHER_KEY)
                .filter(weather -> weather != null && "ok".equals(weather.mWeathers.get(0).status))
                .map(weather -> weather.mWeathers.get(0))
                .doOnError(RetrofitSingleton::disposeFailureInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);

        //配置下面这些会导致崩溃,无法捕捉的,还是用filter来过滤好一点
//            .flatMap(weather -> {
//            String status = weather.mWeathers.get(0).status;
//            if ("no more requests".equals(status)) {
//                return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
//            } else if ("unknown city".equals(status)) {
//                return Observable.error(new RuntimeException(String.format("API没有%s", city)));
//            } else if ("param invalid".equals(status)) {
//                return Observable.error(new RuntimeException("API没有缺少查询的参数"));
//            }
//            return Observable.just(weather);
//        })
    }


    /**
     * Mob 天气状态吗:10001 -->配置Mob的appkey不合法
     * Mob 天气状态吗:10020 -->接口维护
     * Mob 天气状态吗:10021 -->接口停用
     * Mob 天气状态吗:20402 -->查询不到该城市的天气
     */
    public Observable<MobWeather> fetchMobWeather(String city) {
        return sApiService.mMobWeatherAPI(city, C.MOB_APP_KEY)
                .filter(weather -> weather != null && weather.retCode == 200)
                .map(weather -> weather.mMobWeathers.get(0))
                .doOnError(RetrofitSingleton::disposeFailureInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);

        //配置下面这些会导致崩溃,无法捕捉的,还是用filter来过滤好一点
//            .flatMap(weather -> {
//            int code = weather.retCode;
//            if (10001 == code) {
//                return Observable.error(new RuntimeException("配置Mob的appkey不合法"));
//            } else if (10020 == code) {
//                return Observable.error(new RuntimeException("接口维护"));
//            } else if (10021 == code) {
//                return Observable.error(new RuntimeException("接口停用"));
//            } else if (20402 == code) {
//                return Observable.error(new RuntimeException("查询不到该城市的天气"));
//            }
//            return Observable.just(weather);//code==200
//        })
    }


    public Observable<Version> fetchVersion() {
        return sApiService.mVersionAPI(C.FIR_API_APPID, C.FIR_API_TOKEN)
                .doOnError(RetrofitSingleton::disposeFailureInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);
    }

}
