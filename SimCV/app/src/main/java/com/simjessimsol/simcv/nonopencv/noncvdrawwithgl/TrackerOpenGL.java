package com.simjessimsol.simcv.nonopencv.noncvdrawwithgl;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class TrackerOpenGL extends Activity {

    OpenGLSurfaceView glSurfaceView;
    CameraView cameraView;

    //TODO: endre camera, lagre tegning, beholde tegning onStateChanged?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        glSurfaceView = new OpenGLSurfaceView(this);
        setContentView(glSurfaceView);

        cameraView = new CameraView(this, glSurfaceView);
        addContentView(cameraView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

}
