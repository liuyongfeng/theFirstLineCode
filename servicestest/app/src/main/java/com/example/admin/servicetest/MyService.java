package com.example.admin.servicetest;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.example.admin.servicetest.R.*;
/*
http://blog.csdn.net/guolin_blog/article/details/11952435
*/

//Service其实是运行在主线程里的（跟MainActivity是同一个进展）,耗时的过程最好开子进程进行处理，
// 避免阻塞主进程,导致ANR。
// 实现后台下载功能，在activity中可以决定何时开始下载，以及随时查看下载进展。
public class MyService extends Service {
    private static final String TAG = "MyService";

    //创建一个DownloadBinder实例
    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }
    // onCreate()方法只在Service第一次被创建的时候调用，
    // 如果当前Service已经被创建过了，不管怎样调用startService()方法，onCreate()方法都不会再执行
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(){
        super.onCreate();

        //创建通知
        Notification notification = new Notification(drawable.ic_launcher,
                "有通知到来", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "这是通知的标题", "这是通知的内容",
                pendingIntent);
        startForeground(1, notification);
        Log.d(TAG, "onCreate() executed");
        Log.d(TAG, "onCreate: MyService thread id is " + Thread.currentThread().getId());
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onStartCommand: ");
            }
        }).start();

        return super.onStartCommand(intent,flag,startId);
    }

    @Override
    public void onDestroy(){
        //一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
    //这个onBind方法其实就是用于和Activity建立关联的
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    //新增了一个MyBinder类继承自Binder类，然后在MyBinder中添加了一个startDownload()方法用于在后台执行下载任务
    class DownloadBinder extends Binder{

        public void startDownload(){
            //执行具体的下载任务
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "startDownload: executed");
                }
            }).start();
        }

        public int getProgress(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //查看下载进度
                    Log.d(TAG, "getProgress: executed");
                }
            }).start();
            return 0;
        }
    }
}
