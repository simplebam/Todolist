package com.yueyue.todolist.modules.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherAPI {

    @SerializedName("HeWeather5")
    @Expose
    public List<Weather> mWeathers = new ArrayList<>();
}
