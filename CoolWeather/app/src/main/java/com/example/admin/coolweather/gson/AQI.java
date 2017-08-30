package com.example.admin.coolweather.gson;

/**
 * Created by Administrator on 2017/8/30.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
