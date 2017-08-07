package com.example.administrator.boardcastbestpriactice2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";//logt + TAB
    private Button forceOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forceOffline = (Button) findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent("com.example.BroadcastBestPractice2.FORCE_OFFLINE");
                sendBroadcast(intent);
                Log.d(TAG, "onClick: send intent com.example.BroadcastBestPractice2.FORCE_OFFLINE"); //logd + TAB
            }
        });
    }
}
