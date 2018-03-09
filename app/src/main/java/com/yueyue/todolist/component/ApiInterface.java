package com.yueyue.todolist.component;

import com.yueyue.todolist.modules.about.domain.Version;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * author : yueyue on 2018/3/9 09:43
 * desc   : 天气查询APi
 */

public interface ApiInterface {

//    String HOST = "https://free-api.heweather.com/v5/";
//    //https://free-api.heweather.com/v5/weather?city=CN101280103&key=b9e05332eea2426fb74de09c14c77227
//    @GET("weather")
//    Observable<WeatherAPI> mWeatherAPI(@Query("city") String city, @Query("key") String key);
//
//    @GET("weather")
//    Flowable<WeatherAPI> mWeatherAPIF(@Query("city") String city, @Query("key") String key);

    //http://api.fir.im/apps/latest/5a7425c1ca87a877b1a81c2a?api_token=7db041d0c3013b63e4bed2a554f02d85
    //fir.im - 版本查询 https://fir.im/docs/version_detection
    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/{appId}")
    Observable<Version> mVersionAPI(@Path("appId") String appId ,@Query("api_token") String api_token);
}
