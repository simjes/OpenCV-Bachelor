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
import android.widget.ImageView;
import android.widget.Toast;

import com.simjessimsol.simcv.CircleDetection;
import com.simjessimsol.simcv.Drawtivity;
import com.simjessimsol.simcv.FaceDetection;
import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.foregroundDetection;

import java.util.HashMap;

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
    private HashMap<String, Integer> dataMap = new HashMap<>();

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

        dataMap.put("Face Detection", R.drawable.round_button_blue);
        dataMap.put("Circle Detection", R.drawable.round_button_red);
        dataMap.put("Foreground Detection", R.drawable.round_button_green);
        dataMap.put("Color Detection", R.drawable.ic_launcher_cv);

        //specify the adapter we want to use
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, isNative, new MainMenuAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent();
                switch (pos) {
                    case 0: // Face Detection
                        if (isNative) {
                            intent = new Intent(MainMenuActivity.this, FaceDetection.class);
                        } else {
                            intent = new Intent(MainMenuActivity.this, FaceDetection.class);
                        }
                        break;
                    case 1: // Circle Detection
                        intent = new Intent(MainMenuActivity.this, CircleDetection.class);
                        break;
                    case 2: // Foreground Detection
                        intent = new Intent(MainMenuActivity.this, foregroundDetection.class);
                        break;
                    case 3: // Color Detection
                        if (isNative) {
                            intent = new Intent(MainMenuActivity.this, Drawtivity.class);
                        } else {
                            intent = new Intent(MainMenuActivity.this, Drawtivity.class);
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Invalid function", Toast.LENGTH_SHORT).show();
                        break;
                }
                startActivity(intent);
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
