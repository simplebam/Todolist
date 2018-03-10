package com.yueyue.todolist.modules.main.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author : yueyue on 2018/3/10 16:27
 * desc   :
 */

public class MobWeatherAPI implements Serializable {

    /**
     * msg : success
     * retCode : 200
     */
    @SerializedName("msg")
    public String msg;
    @SerializedName("retCode")
    public int retCode;
    @SerializedName("result")
    public List<MobWeather> mMobWeathers;
}