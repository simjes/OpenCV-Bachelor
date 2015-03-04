package com.simjessimsol.simcv;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.simjessimsol.simcv.colortracker.Drawtivity;
import com.simjessimsol.simcv.detection.CircleDetection;
import com.simjessimsol.simcv.detection.FaceDetection;
import com.simjessimsol.simcv.detection.foregroundDetection;
import com.simjessimsol.simcv.nonopencv.noncvdrawwithgl.TrackerOpenGL;
import com.simjessimsol.simcv.nonopencv.noncvgl.ColorTrackerNonOpenCV;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startFaceDetectionClick(View view) {
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


    public void startLittGL(View view) {
        Intent intent = new Intent(this, TrackerOpenGL.class);
        startActivity(intent);
    }


}
