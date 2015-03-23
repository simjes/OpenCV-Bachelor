package com.simjessimsol.simcv.menu_main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.simjessimsol.simcv.CircleDetection;
import com.simjessimsol.simcv.Drawtivity;
import com.simjessimsol.simcv.FaceDetection;
import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.foregroundDetection;
import com.simjessimsol.simcv.nonopencv.ColorTrackerNonOpenCV;

/**
 * Created by Simen Sollie on 25.02.2015.
 * <p/>
 * http://javatechig.com/android/android-recyclerview-example
 * https://developer.android.com/training/material/lists-cards.html
 * http://www.survivingwithandroid.com/2014/11/a-guide-to-android-recyclerview-cardview.html
 */
public class MainMenuActivity extends ActionBarActivity { //ActionBarActivity
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private Switch switchAlternative;

    private String[] dataSet = {"Face Detection", "Circle Detection", "Foreground Detection", "Color Detection"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_menu_main);
        recyclerView = (RecyclerView) findViewById(R.id.container);

        //improves performance if we know that changes in content doesn't change layout
        recyclerView.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //specify the adapter we want to use
        adapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent();
                switch (pos) {
                    case 0: // Face Detection
                        intent = new Intent(MainMenuActivity.this, FaceDetection.class);
                        intent.putExtra("isAlternativeCamera", isAlternative()); //switchAlternative.isChecked()
                        break;
                    case 1: // Circle Detection
                        intent = new Intent(MainMenuActivity.this, CircleDetection.class);
                        break;
                    case 2: // Foreground Detection
                        intent = new Intent(MainMenuActivity.this, foregroundDetection.class);
                        break;
                    case 3: // Color Detection
                        if (isAlternative()) {
                            intent = new Intent(MainMenuActivity.this, ColorTrackerNonOpenCV.class);
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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public boolean isAlternative() {
        switchAlternative = (Switch) findViewById(R.id.switchAlternative);
        return switchAlternative.isChecked();
    }
}
