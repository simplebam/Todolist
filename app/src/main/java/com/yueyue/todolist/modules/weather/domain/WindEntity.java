package com.yueyue.todolist.modules.weather.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WindEntity implements Serializable {
    @SerializedName("deg")
    public String deg;
    @SerializedName("dir")
    public String dir;
    @SerializedName("sc")
    public String sc;
    @SerializedName("spd")
    public String spd;
}