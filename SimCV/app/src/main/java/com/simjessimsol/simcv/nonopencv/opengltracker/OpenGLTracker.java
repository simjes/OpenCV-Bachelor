package com.simjessimsol.simcv.nonopencv.opengltracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public void saveBitmap(Bitmap bm) {
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path, "testbilde.jpg"); // the File to save to
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap pictureBitmap = bm; // obtaining the Bitmap
        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // do not forget to close the stream

        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
