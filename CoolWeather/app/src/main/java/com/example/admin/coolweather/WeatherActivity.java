package com.example.admin.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.PerformanceTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.coolweather.db.City;
import com.example.admin.coolweather.gson.AQI;
import com.example.admin.coolweather.gson.Forecast;
import com.example.admin.coolweather.gson.Weather;
import com.example.admin.coolweather.service.AutoUpdateService;
import com.example.admin.coolweather.util.HttpUtil;
import com.example.admin.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.admin.coolweather.R.color.colorPrimary;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    public SwipeRefreshLayout swipeRefresh;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private Button navButton;
    private String mWeatherId;
    private ImageView imageView;
    public DrawerLayout drawerLayout;

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume: ");
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Log.d(TAG, "onResume: refresh weather use perferences");
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            mWeatherId = weather.basic.weatherId;
            loadBingPic();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
        //初始化各个控件
        initWeather();
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        navButton = (Button)findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout)findViewById(R.id.draw_layout);

        imageView = (ImageView)findViewById(R.id.bing_pic_image);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            mWeatherId = weather.basic.weatherId;
            loadBingPic();
        }else{
            //无缓存时从网络获取天气数据
            String mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void requestWeather(final String weatherId){
        loadBingPic();
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        Log.d(TAG, "requestWeather: " + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "request weather info failed", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                Log.d(TAG, "onResponse: " + responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void loadBingPic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String bingPic = prefs.getString("BingPic", null);
        Log.d(TAG, "loadBingPic: " + bingPic);
        if (bingPic != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: bingPic != null, load bingPic");
                    Glide.with(WeatherActivity.this).load(bingPic).into(imageView);
                }
            });
        }else{
            String requestBingPic = "http://guolin.tech/api/bing_pic";
            HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String responseBingPic = response.body().string();
                    Log.d(TAG, "onResponse: " + responseBingPic);
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("BingPic", responseBingPic);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: bingPic == null, load bingPic from internet");
                            Glide.with(WeatherActivity.this).load(responseBingPic).into(imageView);
                        }
                    });
                }
            });
        }
    }

    public void showWeatherInfo(Weather weather){
        String CityName = weather.basic.cityName;
        String UpdateTime = weather.basic.update.updateTime.split(" ")[1] + "更新";
        String Degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(CityName);
        titleUpdateTime.setText(UpdateTime);
        degreeText.setText(Degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast :weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView info = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            info.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if ( weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动指数：" + weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        /*启动后台天气更新服务*/
        Intent intentService = new Intent(this, AutoUpdateService.class);
        startService(intentService);
    }

    private void initWeather(){
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
    }
}
