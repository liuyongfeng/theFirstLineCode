package com.example.administrator.networktest;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView responseText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequestWithHttpURLConnection = (Button)findViewById(R.id.sendRequestWithHttpURLConnection);
        sendRequestWithHttpURLConnection.setOnClickListener(this);

        Button sendRequestWithOkHttp = (Button)findViewById(R.id.sendRequestWithOkHttp);
        sendRequestWithOkHttp.setOnClickListener(this);
        responseText = (TextView)findViewById(R.id.response_text);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sendRequestWithHttpURLConnection:
                sendRequestWithHttpURLConnection();
                break;
            case R.id.sendRequestWithOkHttp:
                sendRequestWithOkHttp();
                break;
            default:
                break;
        }
    }

    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://127.0.0.1/get_data.xml")//apache server
                        .build();
                try{
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().toString();
                    showResponse(responseData);
                    parseXMLWithPull(responseData);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithPull(String xmlData) {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        //开启新进程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://baidu.com");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                        showResponse(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //将结果显示到UI上
                responseText.setText(response);
            }
        });

    }
}
