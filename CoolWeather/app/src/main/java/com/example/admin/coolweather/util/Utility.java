package com.example.admin.coolweather.util;

import android.text.TextUtils;

import com.example.admin.coolweather.db.City;
import com.example.admin.coolweather.db.Country;
import com.example.admin.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Provider;

/**
 * Created by admin on 2017-8-29.
 * 解析全国省市县数据,
 */

public class Utility {
    /**
     * 解析服务器返回的省级数据,http://guolin.tech/api/china
     */
    public static boolean handProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i=0; i<allProvince.length(); i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的市级数据
     */

    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i=0; i<allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceCode(provinceId);
                    city.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析服务器返回的所有县
     */
    public static boolean handleCountryResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCountry = new JSONArray(response);
                for (int i=0; i<allCountry.length(); i++){
                    JSONObject countryObject = allCountry.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryCode(countryObject.getInt("id"));
                    country.setCountryName(countryObject.getString("name"));
                    country.setCityCode(cityId);
                    country.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
