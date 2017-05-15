package com.example.admin.activitytest;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends BaseActivity {
    private static final String TAG = "SecondActivity";

    public static void actionStart(Context context,String data1,String data2){
        Intent intent = new Intent(context,SecondActivity.class);
        intent.putExtra("param1",data1);
        intent.putExtra("param2",data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        Log.d(TAG, "Task id is " + getTaskId());
        //获取上一个活动传递过来的数据
        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");
        Log.d(TAG, "onCreate: "+ data);
        Log.d(TAG, "onCreate: " + intent.getStringExtra("param1"));
        Log.d(TAG, "onCreate: " + intent.getStringExtra("param2"));
        Toast.makeText(SecondActivity.this,data,Toast.LENGTH_SHORT).show();

        //返回数据给上一个活动
        Button button = (Button)findViewById(R.id.button2);
        //singleInstance 范例
        Button button1 = (Button)findViewById(R.id.button8);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_singleInstance = new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent_singleInstance);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.putExtra("data_return","Hello FirstActivity");
                setResult(RESULT_OK,intent2);
                Log.d(TAG, "onClick: " + "Hello FirstActivity");
                finish();
            }
        });
    }
}
