package com.example.admin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017-8-28.
 */

public class Province extends DataSupport {

    private String ProvinceName;
    private int ProvinceCode;

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        ProvinceCode = provinceCode;
    }
}
