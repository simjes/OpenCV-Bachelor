package com.simjessimsol.simcv.nonopencv.opengltracker2;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


public class OpenGLTrackerActivity extends Activity {
    private final static String TAG = "opengltracker2";

    private OpenGLSurfaceView openGLSurfaceView;
    private MyCamera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        camera = new MyCamera();
        openGLSurfaceView = new OpenGLSurfaceView(this, camera);

        setContentView(openGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopCamera();
    }

}
