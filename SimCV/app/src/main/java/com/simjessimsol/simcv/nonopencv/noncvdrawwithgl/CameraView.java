package com.simjessimsol.simcv.nonopencv.noncvdrawwithgl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class CameraView extends SurfaceView implements Callback, PreviewCallback {
    private final static String TAG = "noncvdrawwithgl";

    private Camera camera;
    private Parameters cameraParameters;
    private int[] optimalResolution;
    private int[] rgbFrame;
    private int width;
    private int height;

    private OpenGLSurfaceView openGLSurfaceView;

    public CameraView(Context context, OpenGLSurfaceView openGLSurfaceView) {
        super(context);
        this.openGLSurfaceView = openGLSurfaceView;
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        cameraParameters = camera.getParameters();
        optimalResolution = findOptimalResolution();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int unusedWidth, int unusedHeight) {
        cameraParameters.setPreviewSize(optimalResolution[0], optimalResolution[1]);
        rgbFrame = new int[optimalResolution[0] * optimalResolution[1]];
        width = optimalResolution[0];
        height = optimalResolution[1];

        camera.setParameters(cameraParameters);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        camera.setPreviewCallback(this);
        openGLSurfaceView.setViewPort(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        YUV_NV21_TO_RGB(rgbFrame, data, width, height);
        Point redCenterOfMass = findRedCenterOfMass();
        if (redCenterOfMass.x > 0 && redCenterOfMass.y > 0) {
            VertexPoint vertexPoint = pointToVertexPoint(redCenterOfMass);
            openGLSurfaceView.addPointsToDraw(vertexPoint);
            openGLSurfaceView.requestRender();
        }
    }

    private VertexPoint pointToVertexPoint(Point point) {
        float x = (float) point.x / width;
        float y = (float) point.y / height;
        if (x < 0.5f) {
            x = (x * 2) - 1f;
        } else {
            x = Math.abs(1f - (x * 2));
        }
        if (y < 0.5f) {
            y = 1f - (y * 2);
        } else {
            y = -((y * 2) - 1f);
        }
        Log.i(TAG, "vertex point, x: " + x + ", y: " + y);
        return new VertexPoint(x, y);
    }

    private Point findRedCenterOfMass() {
        Date first = new Date();
        Point average = new Point(0, 0);
        int nrOfPoints = 0;
        for (int i = 0; i < rgbFrame.length; i++) {
            int pixelVal = rgbFrame[i];
            if (Color.red(pixelVal) >= 200 && Color.green(pixelVal) <= 45 && Color.blue(pixelVal) <= 45) {
                rgbFrame[i] = 0xffffff;
            } else {
                rgbFrame[i] = 0;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rgbFrame[y * width + x] > 0) {
                    average.x += x;
                    average.y += y;
                    nrOfPoints++;
                }
            }
        }
        if (nrOfPoints > 0) {
            average.x /= nrOfPoints;
            average.y /= nrOfPoints;
        }
        Log.d(TAG, "slutt: " + (new Date().getTime() - first.getTime()));
        return average;
    }

    //TODO: flytt til annen klasse? statisk?
    private int[] findOptimalResolution() {
        List<Size> supportedCameraSizes = cameraParameters.getSupportedPreviewSizes();
        int optimalWidth = 0;
        int optimalHeight = 0;

        for (Size s : supportedCameraSizes) {
            Log.d(TAG, "optimal, size width: " + s.width + ", size height: " + s.height);

            if (s.width <= getWidth() && s.height <= getHeight()) {
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

    //TODO: flytt til annen klasse? statisk?
    //TODO: copied from http://stackoverflow.com/questions/12469730/confusion-on-yuv-nv21-conversion-to-rgb
    public static void YUV_NV21_TO_RGB(int[] argb, byte[] yuv, int width, int height) {
        final int frameSize = width * height;

        final int ii = 0;
        final int ij = 0;
        final int di = +1;
        final int dj = +1;

        int a = 0;
        for (int i = 0, ci = ii; i < height; ++i, ci += di) {
            for (int j = 0, cj = ij; j < width; ++j, cj += dj) {
                int y = (0xff & ((int) yuv[ci * width + cj]));
                int v = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 0]));
                int u = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = (int) (1.164f * (y - 16) + 1.596f * (v - 128));
                int g = (int) (1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = (int) (1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                argb[a++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
    }
}
