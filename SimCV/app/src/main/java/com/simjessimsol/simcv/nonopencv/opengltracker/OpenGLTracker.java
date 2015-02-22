package com.simjessimsol.simcv.nonopencv.opengltracker;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.WindowManager;

public class OpenGLTracker extends Activity implements SurfaceTexture.OnFrameAvailableListener {

    private OpenGLSurfaceView glSurfaceView;
    private SurfaceTexture surfaceTexture;
    private Camera camera;
    private OpenGLRenderer openGLRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        glSurfaceView = new OpenGLSurfaceView(this);
        openGLRenderer = glSurfaceView.getRenderer();
        setContentView(glSurfaceView);
    }

    public void startCamera(int texture) {
        surfaceTexture = new SurfaceTexture(texture);
        surfaceTexture.setOnFrameAvailableListener(this);
        openGLRenderer.setSurfaceTexture(surfaceTexture);

        camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        glSurfaceView.requestRender();
    }

    @Override
    protected void onPause() {
        camera.stopPreview();
        camera.release();
        super.onPause();
    }
}
