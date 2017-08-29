package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017-8-28.
 */

public class City extends DataSupport {

    private String CityName;
    private int CityCode;
    private int ProvinceCode;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        ProvinceCode = provinceCode;
    }
}
