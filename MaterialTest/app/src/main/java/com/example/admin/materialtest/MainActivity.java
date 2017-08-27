package com.example.admin.materialtest;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Runnable {

    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private fruit[] fruits = {
            new fruit("apple",R.drawable.apple),
            new fruit("fruits",R.drawable.fruits),
            new fruit("kiwifruit",R.drawable.kiwifruit),
            new fruit("orange",R.drawable.orange),
            new fruit("pomegranate",R.drawable.pomegranate),
            new fruit("strawberry",R.drawable.strawberry),
    };

    private List<fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //导航页
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //悬浮按钮
        FloatingActionButton buttonFab = (FloatingActionButton)findViewById(R.id.fab);
        buttonFab.setOnClickListener(this);
        //显示水果图片
        Thread thread=new Thread(this);
        thread.start();
        //刷新
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener(){
            @Override
            public void onRefresh(){
                refreshFruites();
            }
        });
     }
    private void refreshFruites(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    @Override
    public void run() {
        initFruits();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }

    private void initFruits(){
        fruitList.clear();
        for (int i=0; i<50; i++){
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.backup:
                Toast.makeText(this,"You clicked Backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"You clicked Deleted",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"You clicked Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                Toast.makeText(this,"You clicked home",Toast.LENGTH_SHORT).show();
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fab:
                //Toast.makeText(this,"fab clicked",Toast.LENGTH_SHORT).show();
                Snackbar.make(view,"Critical data deleted",Snackbar.LENGTH_SHORT)
                        .setAction("Undo",new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                            Toast.makeText(MainActivity.this,"Data Restored",
                                        Toast.LENGTH_SHORT).show();
                             }
                }).show();
                break;
            default:
                break;
        }
    }

}
