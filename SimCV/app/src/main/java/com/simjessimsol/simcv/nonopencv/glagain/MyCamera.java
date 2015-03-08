package com.simjessimsol.simcv.nonopencv.glagain;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;

import java.io.IOException;

public class MyCamera {
    private final static String TAG = "opengltracker2";

    private Camera camera;
    private Parameters parameters;
    private boolean running = false; //TODO: unodvendig?


    public void startCamera(SurfaceTexture surface, OpenGLSurfaceView surfaceView) {
        Log.i(TAG, "Camera starting");

        camera = Camera.open(0);
        parameters = camera.getParameters();
        int[] optimalSize = surfaceView.findOptimalResolution(parameters);
        parameters.setPreviewSize(optimalSize[0], optimalSize[1]);
        camera.setParameters(parameters);
        Log.i(TAG, "param width: " + camera.getParameters().getPreviewSize().width + ", height: " + camera.getParameters().getPreviewSize().height);

        try {
            camera.setPreviewTexture(surface);
            camera.startPreview();
            running = true; //TODO: unodvendig?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopCamera() {
        if (running) {
            Log.i(TAG, "Camera stopping");
            camera.stopPreview();
            camera.release();
            running = false;
        }
    }
}