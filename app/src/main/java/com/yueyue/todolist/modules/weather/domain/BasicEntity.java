package com.yueyue.todolist.modules.weather.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author : yueyue on 2018/3/10 01:31
 * desc   :
 */

public class BasicEntity implements Serializable {
    @SerializedName("city")
    public String city;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;

    }
}