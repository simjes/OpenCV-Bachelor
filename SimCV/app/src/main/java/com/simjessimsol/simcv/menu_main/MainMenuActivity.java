package com.simjessimsol.simcv.menu_main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

/**
 * Created by Simen on 25.02.2015.
 *
 * http://javatechig.com/android/android-recyclerview-example
 */
public class MainMenuActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Boolean isNative = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.container);

        //improves performance if we know that changes in content doesn't change layout
        mRecyclerView.setHasFixedSize(true);

        //we use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] dataSet = {"Face Detection", "Circle Detection", "Foreground Detection", "Color Detection"};
        ToggleButton toggleNative = (ToggleButton) findViewById(R.id.toggleNative);
        //toggleNative.setClickable(isNative);

        //specify the adapter we want to use
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, dataSet, isNative);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void startFunctionClick(View view) {
        TextView txtView = (TextView) findViewById(R.id.title);
        String txtFunction = txtView.getText().toString();

        Toast.makeText(getApplicationContext(), txtView.getText(), Toast.LENGTH_SHORT).show();
    }

    /*public void startFaceDetectionClick(View view) {
        Intent intent = new Intent(this, FaceDetection.class);
        startActivity(intent);
    }

    public void startCircleDetectionClick(View view) {
        Intent intent = new Intent(this, CircleDetection.class);
        startActivity(intent);
    }

    public void startDrawingClick(View view) {
        Intent intent = new Intent(this, Drawtivity.class);
        startActivity(intent);
    }

    public void startForegroundDetection(View view) {
        Intent intent = new Intent(this, foregroundDetection.class);
        startActivity(intent);
    }

    public void startNonCVColorTracking(View view) {
        Intent intent = new Intent(this, ColorTrackerNonOpenCV.class);
        startActivity(intent);
    }

    public void startOpenGlTracker(View view) {
        Intent intent = new Intent(this, OpenGLTracker.class);
        startActivity(intent);
    }*/

}
