package com.simjessimsol.simcv.menu_main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.simjessimsol.simcv.CircleDetection;
import com.simjessimsol.simcv.Drawtivity;
import com.simjessimsol.simcv.FaceDetection;
import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.foregroundDetection;

/**
 * Created by Simen Sollie on 25.02.2015.
 * <p/>
 * http://javatechig.com/android/android-recyclerview-example
 * https://developer.android.com/training/material/lists-cards.html
 * http://www.survivingwithandroid.com/2014/11/a-guide-to-android-recyclerview-cardview.html
 */
public class MainMenuActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;

    private Boolean isNative = false;
    private String[] dataSet = {"Face Detection", "Circle Detection", "Foreground Detection", "Color Detection"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_menu_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.container);

        //improves performance if we know that changes in content doesn't change layout
        mRecyclerView.setHasFixedSize(true);



        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //specify the adapter we want to use
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, isNative, new MainMenuAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent();
                if (pos == 0){
                    intent = new Intent(MainMenuActivity.this, FaceDetection.class);
                } else if (pos == 1){
                    intent = new Intent(MainMenuActivity.this, CircleDetection.class);
                } else if (pos == 2){
                    intent = new Intent(MainMenuActivity.this, foregroundDetection.class);
                } else if (pos == 3){
                    intent = new Intent(MainMenuActivity.this, Drawtivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid function", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
