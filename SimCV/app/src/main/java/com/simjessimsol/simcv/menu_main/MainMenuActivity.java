package com.simjessimsol.simcv.menu_main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.simjessimsol.simcv.detection.CircleDetection;
import com.simjessimsol.simcv.colortracker.Drawtivity;
import com.simjessimsol.simcv.detection.FaceDetection;
import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.detection.ForegroundDetection;
import com.simjessimsol.simcv.menu_overflow.AboutDialogFragment;
import com.simjessimsol.simcv.menu_overflow.HelpDialogFragment;
import com.simjessimsol.simcv.nonopencv.noncvdrawwithgl.TrackerOpenGL;

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

    private String[] dataSet = {"Face\nDetection", "Circle\nDetection", "Foreground\nDetection",
            "Color\nTracker", "OpenGL\nTracker"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.layout_container);
        recyclerView = (RecyclerView) findViewById(R.id.container);

        //improves performance if we know that changes in content doesn't change layout
        recyclerView.setHasFixedSize(true);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //specify the adapter we want to use
        adapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, int scale) {
                Intent intent = new Intent();
                switch (pos) {
                    case 0: // Face Detection
                        intent = new Intent(MainMenuActivity.this, FaceDetection.class);
                        intent.putExtra("isAlternativeCamera", isAlternative()); //switchAlternative.isChecked()
                        intent.putExtra("setScale", scale);
                        break;
                    case 1: // Circle Detection
                        intent = new Intent(MainMenuActivity.this, CircleDetection.class);
                        intent.putExtra("setScale", scale);
                        break;
                    case 2: // Foreground Detection
                        intent = new Intent(MainMenuActivity.this, ForegroundDetection.class);
                        intent.putExtra("setScale", scale);
                        break;
                    case 3: // Color Tracker
                        intent = new Intent(MainMenuActivity.this, Drawtivity.class);
                        intent.putExtra("setScale", scale);
                        break;
                    case 4: // OpenGL Color Tracker
                        intent = new Intent(MainMenuActivity.this, TrackerOpenGL.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_help:
                HelpDialogFragment helpFragment = new HelpDialogFragment();
                helpFragment.show(fragmentManager, String.valueOf(R.string.dialog_title_help));
                return true;
            case R.id.action_about:
                AboutDialogFragment aboutFragment = new AboutDialogFragment();
                aboutFragment.show(fragmentManager, String.valueOf(R.string.dialog_title_about));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isAlternative() {
        switchAlternative = (Switch) findViewById(R.id.switchAlternative);
        return switchAlternative.isChecked();
    }
}
