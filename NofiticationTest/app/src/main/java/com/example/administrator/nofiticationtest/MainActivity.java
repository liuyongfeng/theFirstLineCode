package com.example.administrator.nofiticationtest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice = (Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.send_notice:
                Intent intent = new Intent(this,NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(this);
                builder.setContentTitle("Title");
                builder .setContentText("Text");
                builder.setWhen(System.currentTimeMillis());
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                builder.setContentIntent(pi);
                //通知声音
                builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                //振动、呼吸灯提醒
                long [] vibrates = {0,1000,1000,1000};
                builder.setVibrate(vibrates);
                builder.setLights(Color.GREEN,1000,1000);
                //builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.big_image)));
                //点击通知后自动消失
               // builder.setAutoCancel(true);
                //发送通知
                manager.notify(1,builder.build());//手机通知管理里面设置允许才会有通知提示。（nubia NX549J)
                break;
            default:
                break;
        }
    }
}

