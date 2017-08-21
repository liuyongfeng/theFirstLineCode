package com.example.administrator.servicebestpractice;

import android.accessibilityservice.AccessibilityService;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Process;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.webkit.DownloadListener;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ResponseCache;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/21.
 */
// Integer表示进度；Integer表示结果
public class DownloadTask extends AsyncTask<String, Integer, Integer>{
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSE = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isCancelled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener){
       this.listener = listener;
    }

    @Override
    protected  void onPreExecute(){

    }
    //在后台具体执行下载逻辑
    @Override
    protected Integer doInBackgroud(String... params){
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;

       try{
           long downloadedLength = 0;//记录已下载的文件长度
           String downloadUrl =  params[0];
           //从Url下载链接中解析出文件名
           String fineName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
           String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
           file  = new File(directory + fineName);

           if (file.exists()){
               downloadedLength = file.length();
           }
           long contentLength = getContentLength();
           if (contentLength == 0){
               return TYPE_FAILED;
           }else if(contentLength == downloadedLength){
               return TYPE_SUCCESS;
           }
           OkHttpClient client = new OkHttpClient();
           Request request = new Request.Builder()
                   .addHeader("RANGE","bytes=" + downloadedLength + "-")
                   .url(downloadUrl)
                   .build();
           Response response = client.newCall(request).execute();
           if (response != null){

           }
       }catch (Exception e){
           e.printStackTrace();
           return TYPE_FAILED;
       }
       return TYPE_SUCCESS;
    }
    //在界面显示当前的下载进度
    @Override
    protected  void onProgressUpdate(Integer... values){

    }
    //通知下载结果
    @Override
    protected void onPostExecute(Boolean result){

    }
}
