package com.simjessimsol.simcv.nonopencv;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


public class ColorTrackerNonOpenCV extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(new DisplayCamera(this));
    }

}