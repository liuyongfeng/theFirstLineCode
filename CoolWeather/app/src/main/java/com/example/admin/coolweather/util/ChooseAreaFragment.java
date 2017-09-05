package com.example.admin.coolweather.util;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.coolweather.MainActivity;
import com.example.admin.coolweather.R;
import com.example.admin.coolweather.WeatherActivity;
import com.example.admin.coolweather.db.City;
import com.example.admin.coolweather.db.County;
import com.example.admin.coolweather.db.Province;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/3.
 */

public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /*省列表*/
    private List<Province> provinceList;
    /*市列表*/
    private List<City> cityList;
    /*县列表*/
    private List<County> countyList;

    /*选中的省份*/
    private Province selectedProvince;
    /*选中的市*/
    private City selectedCity;

    /*当前选中的级别*/
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        }
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(1): currentLevel = " + currentLevel);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentLevel) {
                    case LEVEL_PROVINCE:
                        selectedProvince = provinceList.get(position);
                        queryCities();
                        break;
                    case LEVEL_CITY:
                        selectedCity = cityList.get(position);
                        queryCounties();
                        break;
                    case LEVEL_COUNTY:
                        /*跳转到天气界面*/
                        String weatherId = countyList.get(position).getWeatherId();//page 534
                        if (getActivity() instanceof MainActivity){
                                                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                            intent.putExtra("weather_id",weatherId);
                            startActivity(intent);
                            Log.d(TAG, "onItemClick: MainActivity");
                            getActivity().finish();

                        }else if (getActivity() instanceof WeatherActivity){
                            WeatherActivity activity = (WeatherActivity)getActivity();
                            activity.drawerLayout.closeDrawers();
                            activity.swipeRefresh.setRefreshing(true);
                            activity.requestWeather(weatherId);
                            Log.d(TAG, "onItemClick: WeatherActivity");
                        }

                        break;
                    default:
                        break;
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        Log.d(TAG, "onActivityCreated(2): currentLevel = " + currentLevel);
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);

        /*try to get provinces form database*/
        provinceList = DataSupport.findAll(Province.class);
        Log.d(TAG, "queryProvinces: provinceList.size() = " + provinceList.size());
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
                Log.d(TAG, "queryProvinces: " + province.getProvinceName());
            }
            adapter.notifyDataSetInvalidated();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        Log.d(TAG, "queryCities: cityList.size() = " + cityList.size());
        if(cityList.size() > 0){
            dataList.clear();//修复list内容不更新的问题
           for (City city : cityList){
               dataList.add(city.getCityName());
               Log.d(TAG, "queryCities: " + city.getCityName());
           }
           adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        Log.d(TAG, "queryCounties: countyList.size() = " + countyList.size() + "selectedCity.getId() = " + selectedCity.getId());
        // countyList.size() = 0 导致死循环
        Log.d(TAG, "queryCounties: countyList.isEmpty() = " + countyList.isEmpty());
        if(!countyList.isEmpty()){
            dataList.clear();//修复list内容不更新的问题
            for (County county : countyList){
                dataList.add(county.getCountyName());
                Log.d(TAG, "queryCounties: " + county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
            Log.d(TAG, "queryCounties: currentLevel = " + currentLevel);
        }else{
            Log.d(TAG, "queryCounties: get from server" + selectedProvince.getProvinceName() + selectedCity.getCityName());
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"
                    + provinceCode + "/" + cityCode;
            Log.d(TAG, "queryCounties: " + address);
            queryFromServer(address,"county");
        }
    }

    public void queryFromServer(String address, final String type) {
        showProgressDialog();
        Log.d(TAG, "queryFromServer: address = " + address + " ,type = " + type);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Toast.makeText(getContext(),"Load failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    /*show progress*/
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
