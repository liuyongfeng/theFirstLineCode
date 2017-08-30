package com.example.admin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/30.
 */

public class Suggestion {
    @SerializedName("comf")
    public Confort comf;

    @SerializedName("cw")
    public Cw cw;

    @SerializedName("sport")
    public Sport sport;

    class Confort {
        @SerializedName("txt")
        public String info;
    }

    class Cw {
        @SerializedName("txt")
        public String info;
    }

    class Sport{
        @SerializedName("txt")
        public String info;
    }
}
