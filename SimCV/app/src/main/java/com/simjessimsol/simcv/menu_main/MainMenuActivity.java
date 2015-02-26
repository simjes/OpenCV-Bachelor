package com.simjessimsol.simcv.menu_main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.simjessimsol.simcv.CircleDetection;
import com.simjessimsol.simcv.Drawtivity;
import com.simjessimsol.simcv.FaceDetection;
import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.foregroundDetection;
import com.simjessimsol.simcv.nonopencv.ColorTrackerNonOpenCV;
import com.simjessimsol.simcv.nonopencv.opengltracker.OpenGLTracker;

import java.util.HashMap;

/**
 * Created by Simen Sollie on 25.02.2015.
 * <p/>
 * https://developer.android.com/training/material/lists-cards.html
 * http://www.survivingwithandroid.com/2014/11/a-guide-to-android-recyclerview-cardview.html
 */
public class MainMenuActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Boolean isNative = false;
    private String[] dataSet = {"Face Detection", "Circle Detection", "Foreground Detection", "Color Detection"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.container);

        //improves performance if we know that changes in content doesn't change layout
        mRecyclerView.setHasFixedSize(true);

        //specify the adapter we want to use
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, isNative, new MainMenuAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                //String s = Integer.toString(pos);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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

        //we use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
