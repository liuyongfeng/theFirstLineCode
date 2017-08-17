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
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private MyService.DownloadBinder downloadBinder;
    //让MainActivity和MyService之间建立关联,这里我们首先创建了一个ServiceConnection的匿名类
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        //Activity与Service建立关联的时候调用
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        //Activity与Service解除关联的时候调用
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
        Log.d(TAG, "onCreate: MainActivity thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.start_service:
                //通过intend在activity中启动MyService服务
                Intent start_intend = new Intent(this,MyService.class);
                //调用startService()方法来启动MyService
                startService(start_intend);
                break;
            case R.id.stop_service:
                //通过intend在activity中停止MyService服务，
                // 注意，每个service都要在AndroidManifest.xml中注册
                Intent stop_intend = new Intent(this,MyService.class);
                //调用stopService()方法来停止MyService
                stopService(stop_intend);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this,MyService.class);
                //BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service，
                // 这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行。
                bindService(bindIntent,connection,BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
            default:
                break;
        }
    }
}
