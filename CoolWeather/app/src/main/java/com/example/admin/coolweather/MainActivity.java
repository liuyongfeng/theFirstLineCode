package com.example.admin.coolweather;
/**
 *  https://console.heweather.com/my/service
 *  个人认证key: 74e15c6ca6cc4d9fa1ffd044399353a2
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences perfs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if(perfs.getString("weather",null) != null){
            Log.d(TAG, "onCreate: 2");
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }else{

        }
    }
}
