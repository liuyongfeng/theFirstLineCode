package com.example.administrator.databasetest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //数据库对象实例化
        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,3);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dbHelper.getWritableDatabase();//创建或者升级数据库
            }
        });

        Button addDatabase = (Button) findViewById(R.id.add_data);
        addDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("price",16.96);
                values.put("pages",454);
                db.insert("Book",null,values);
            }
        });

    }
}
