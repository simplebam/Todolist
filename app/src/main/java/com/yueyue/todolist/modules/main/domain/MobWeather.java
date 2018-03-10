package com.yueyue.todolist.modules.main.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author : yueyue on 2018/3/10 16:39
 * desc   :
 */

public class MobWeather implements Serializable {
    /**
     * city : 番禺
     * humidity : 湿度：31%
     * temperature : 22℃
     * weather : 晴
     * wind : 东北风2级
     */
    @SerializedName("city")
    public String city;
    @SerializedName("humidity")
    public String humidity;
    @SerializedName("temperature")
    public String temperature;
    @SerializedName("weather")
    public String weather;
    @SerializedName("wind")
    public String wind;
}
