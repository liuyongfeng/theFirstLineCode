package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017-8-28.
 */

public class Country extends DataSupport {

    private String CountryName;

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public int getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(int countryCode) {
        CountryCode = countryCode;
    }

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    private int CountryCode;
    private int CityCode;



}
