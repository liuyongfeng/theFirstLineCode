package com.example.admin.uiwidgettest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private ImageView imageView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
        Button button = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.edit_text);
        imageView = (ImageView)findViewById(R.id.image_view);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
/*                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }*/
                int progress = progressBar.getProgress();
                progress += 10;
                progressBar.setProgress(progress);
                imageView.setImageResource(R.drawable.penguins);

                String inputText = editText.getText().toString();
                Toast.makeText(MainActivity.this,inputText, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
