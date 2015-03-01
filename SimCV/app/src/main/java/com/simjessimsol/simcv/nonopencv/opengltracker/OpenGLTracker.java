package com.simjessimsol.simcv.nonopencv.opengltracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class OpenGLTracker extends Activity implements SurfaceTexture.OnFrameAvailableListener {
    private final static String TAG = "opengltracker";

    private OpenGLSurfaceView glSurfaceView;
    private SurfaceTexture surfaceTexture;
    private Camera camera;
    private OpenGLRenderer openGLRenderer;

    //TODO: fix freeze onPause/onResume
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
        Camera.Parameters parameters = camera.getParameters();
        int[] optimalSize = findOptimalResolution();
        parameters.setPreviewSize(optimalSize[0], optimalSize[1]);
        camera.setParameters(parameters);

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
        glSurfaceView.onPause();
        //super.onPause();
        //TODO: temp fix
        System.exit(0);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }*/

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


    private int[] findOptimalResolution() {
        List<Camera.Size> supportedCameraSizes = camera.getParameters().getSupportedPreviewSizes();
        int optimalWidth = 0;
        int optimalHeight = 0;

        for (Camera.Size s : supportedCameraSizes) {
            Log.d(TAG, "optimal, size width: " + s.width + ", size height: " + s.height);
            //TODO: feil resolution, pga glSurfaceView.getWidth()/height?
            //if (s.width <= glSurfaceView.getWidth() && s.height <= glSurfaceView.getHeight()) {
            if (s.width <= 1300 && s.height <= 1000) {
                if (s.width >= optimalWidth && s.height >= optimalHeight) {
                    optimalWidth = s.width;
                    optimalHeight = s.height;
                }
            }
        }

        Log.d(TAG, "optimal width: " + optimalWidth + ", optimal height: " + optimalHeight);
        int[] optimalSize = new int[2];
        optimalSize[0] = optimalWidth;
        optimalSize[1] = optimalHeight;
        return optimalSize;
    }
}
