package com.simjessimsol.simcv.nonopencv;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.simjessimsol.simcv.R;


public class ColorTrackerNonOpenCV extends Activity {
    private static final String TAG = "com.simjessimsol.simcv";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_color_tracker_non_open_cv);


        SurfaceView displayCamera = new DisplayCamera(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        displayCamera.setLayoutParams(layoutParams);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.colorTrackerNonCvLayout);
        relativeLayout.addView(displayCamera);
    }

}
