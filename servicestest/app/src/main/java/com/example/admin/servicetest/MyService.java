package com.example.admin.servicetest;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
//实现后台下载功能，在activity中可以决定何时开始下载，以及随时查看下载进展。
public class MyService extends Service {
    private static final String TAG = "MyService";

    //创建一个DownloadBinder实例
    private DownloadBinder mBinder = new DownloadBinder();
    //创建一个DownloadBinder类
    class DownloadBinder extends Binder{
        //开始下载
        public void startDownload(){
            Log.d(TAG, "startDownload: executed");
        }
        //查看进度
        public int getProgress(){
            Log.d(TAG, "getProgress: executed");
            return 0;
        }
    }

    public MyService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startId){
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent,flag,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
