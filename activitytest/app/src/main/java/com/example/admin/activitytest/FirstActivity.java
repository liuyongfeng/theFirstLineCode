package com.example.admin.activitytest;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.net.sip.SipErrorCode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends BaseActivity {

    private static final String TAG = "FirstActivity";
    //加载菜单的布局文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    //实现点击对应菜单的动作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "You Clicked add",Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "You Clicked remove",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnedData = data.getStringExtra("data_return");
                    Log.d(TAG, "onActivityResult: " + returnedData);
                    Toast.makeText(this, returnedData, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + this.toString());
        //加载first activity的布局
        setContentView(R.layout.first_layout);
        Log.d(TAG, "Task id is " + getTaskId());
        //引用按钮资源
        Button button1 = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button3);
        Button button3 = (Button)findViewById(R.id.button5);
        Button button4 = (Button)findViewById(R.id.button6);
        Button button5 = (Button)findViewById(R.id.button7);
        Button singleTask = (Button)findViewById(R.id.singleTask);
        Button singleInstance = (Button)findViewById(R.id.singleInstance);

        //实现一个显式的intent
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            //create a toast
            public void onClick(View view) {
            //   Toast.makeText(FirstActivity.this, "You clicked Button", Toast.LENGTH_SHORT).show();
            //    finish();//destroy activity
                //显示intent
        //        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
         //       startActivity(intent);

                SecondActivity.actionStart(FirstActivity.this,"hello","world");
            }
        });

        //实现一个隐式的intent
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐式intent
                Intent intent1 = new Intent("com.example.activitytest.ACTION_START");
                intent1.addCategory("com.example.activitytest.MY_CATEGORY");
                startActivity(intent1);

                //展示一个网页
                //Intent intent3 = new Intent(Intent.ACTION_VIEW);
                //intent3.setData(Uri.parse("http://www.baidu.com"));

                //拨打电话
                Intent intent4 = new Intent(Intent.ACTION_DIAL);
                intent4.setData(Uri.parse("tel:10086"));
                startActivity(intent4);
            }
        });

        //实现通过intent传递数据给其他的activity
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "Hello SecondActivity";
                Intent intent_data = new Intent(FirstActivity.this, SecondActivity.class);
                intent_data.putExtra("extra_data",data);
                startActivity(intent_data);
            }
        });

        //实现返回数据给上一个活动的例子
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_callback = new Intent(FirstActivity.this, SecondActivity.class);
                startActivityForResult(intent_callback,1);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_bootMode = new Intent(FirstActivity.this,ThirdActivity.class);
                startActivity(intent_bootMode);
            }
        });
        singleTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_singleTask = new Intent(FirstActivity.this,ThirdActivity.class);
                startActivity(intent_singleTask);
                Log.d(TAG, "onClick: " + "start ThirdActivity");
            }
        });

        singleInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_singleTask = new Intent(FirstActivity.this,SecondActivity.class);
                startActivity(intent_singleTask);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: " + this.toString());
    }
}
