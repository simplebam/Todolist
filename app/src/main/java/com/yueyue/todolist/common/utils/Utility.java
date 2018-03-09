package com.yueyue.todolist.common.utils;

import com.google.gson.Gson;
import com.yueyue.todolist.modules.weather.domain.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * author : yueyue on 2018/3/9 21:47
 * desc   :
 */

public class Utility {


    /**
     * 将返回的JSON数据解析成Weather实体类,使用了V5接口
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
