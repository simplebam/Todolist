package com.yueyue.todolist.component;

import com.yueyue.todolist.modules.about.domain.Version;
import com.yueyue.todolist.modules.main.domain.MobWeatherAPI;
import com.yueyue.todolist.modules.weather.domain.WeatherAPI;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //----------和风天气-------------
    String HEWEATHER_HOST = "https://free-api.heweather.com/v5/";

    //https://free-api.heweather.com/v5/weather?city=CN101280103&key=b9e05332eea2426fb74de09c14c77227
    @GET("weather")
    Observable<WeatherAPI> mWeatherAPI(@Query("city") String city, @Query("key") String key);


    //------------Fir 内测---------------
    //http://api.fir.im/apps/latest/5a98f70d959d69345043b19f?api_token=9f54d692924d4f867795c3047cf57dc4
    //fir.im - 版本查询 https://fir.im/docs/version_detection
    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/{api_app_id}")
    Observable<Version> mVersionAPI(@Path("api_app_id") String api_id, @Query("api_token") String api_token);

    //-------Mob天气--------
    //http://apicloud.mob.com/v1/weather/query?key=appkey&city=通州&province=北京
    @GET("http://apicloud.mob.com/v1/weather/query")
    Observable<MobWeatherAPI> mMobWeatherAPI(@Query("city") String city, @Query("key") String key);
}
