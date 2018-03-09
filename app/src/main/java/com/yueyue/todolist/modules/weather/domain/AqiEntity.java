package com.yueyue.todolist.modules.weather.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author : yueyue on 2018/3/10 01:30
 * desc   : AQI空气质量
 */

public class AqiEntity implements Serializable {

    @SerializedName("city")
    public CityEntity city;
}