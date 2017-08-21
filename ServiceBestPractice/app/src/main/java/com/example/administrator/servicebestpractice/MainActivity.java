package com.example.administrator.servicebestpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startDownload = (Button)findViewById(R.id.start_download);
        Button pauseDownload = (Button)findViewById(R.id.pause_download);
        Button cancelDownload = (Button)findViewById(R.id.cancel_download);

        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.start_download:
                break;
            case R.id.pause_download:
                break;
            case R.id.cancel_download:
                break;
            default:
                break;
        }
    }
}
