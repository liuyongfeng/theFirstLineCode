package com.example.admin.servicetest;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService = (Button)findViewById(R.id.start_service);
        startService.setOnClickListener(this);
        Button stopService = (Button)findViewById(R.id.stop_service);
        stopService.setOnClickListener(this);
        Button bindService = (Button)findViewById(R.id.bind_service);
        bindService.setOnClickListener(this);
        Button unbindService = (Button)findViewById(R.id.unbind_service);
        unbindService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.start_service:
                Intent start_intend = new Intent(this,MyService.class);
                startService(start_intend);
                break;
            case R.id.stop_service:
                Intent stop_intend = new Intent(this,MyService.class);
                stopService(stop_intend);
                break;
            case R.id.bind_service:
                break;
            case R.id.unbind_service:
                break;
            default:
                break;
        }
    }
}
