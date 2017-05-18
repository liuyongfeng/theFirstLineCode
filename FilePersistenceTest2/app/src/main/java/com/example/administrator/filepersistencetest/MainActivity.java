package com.example.administrator.filepersistencetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText edit;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.edit);
        String intputText = load();
        edit.setText(intputText);
        edit.setSelection(intputText.length());
        Toast.makeText(this,"Restore success",Toast.LENGTH_LONG).show();
    }

    public String load()
    {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }


    public void save(String inputText)
    {
        BufferedWriter writer = null;
        try{
            //通过openFileOutput得到FileOutputStream
            FileOutputStream  out = openFileOutput("data", Context.MODE_PRIVATE);
            //通过FileOutputStream得到OutputStreamWriter，再得到BufferedWriter
            writer = new BufferedWriter(new OutputStreamWriter(out));
            //保存
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

