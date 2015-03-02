package com.simjessimsol.simcv.nonopencv.noncvdrawwithgl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLSurfaceView extends GLSurfaceView implements Renderer {
    private final static String TAG = "noncvdrawwithgl";

    private ArrayList<Point> pointsToDraw;

    public OpenGLSurfaceView(Context context) {
        super(context);
        pointsToDraw = new ArrayList<>();
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //TODO: skifte viewport
        GLES20.glViewport(0, 0, width, height);
        Log.i(TAG, "GLVIEW: width:" + width + ", height: " + height);

    }

    //TODO: adde til surfaceviewen, istedet for å tegne på ny hver gang ?
    @Override
    public void onDrawFrame(GL10 gl) {
        //TODO: bare for testing
        /*float c = 1.0f / 256 * (System.currentTimeMillis() % 256);
        GLES20.glClearColor(c, c, c, 0.5f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);*/
    }

    public void addPointsToDraw(Point point) {
        pointsToDraw.add(point);
        Log.i(TAG, "nyeste point, x: " + pointsToDraw.get(pointsToDraw.size() - 1).x + ", y: " + pointsToDraw.get(pointsToDraw.size() - 1).y);
    }
}
