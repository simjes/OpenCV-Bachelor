package com.simjessimsol.simcv.nonopencv.noncvdrawwithgl;

import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;

import java.util.Date;

//TODO: delete class if not needed

public class FindMassCenter implements Runnable {
    private final static String TAG = "noncvdrawwithgl";

    //private Camera camera;
    private OpenGLSurfaceView openGLSurfaceView;
    private int cameraWidth;
    private int cameraHeight;

    private byte[] yuvFrame;
    private int[] rgbFrame;

    public FindMassCenter(byte[] data, Camera camera, OpenGLSurfaceView openGLSurfaceView) {
        yuvFrame = data;
        this.openGLSurfaceView = openGLSurfaceView;
        //this.camera = camera;
        cameraWidth = camera.getParameters().getPreviewSize().width;
        cameraHeight = camera.getParameters().getPreviewSize().height;
        rgbFrame = new int[cameraWidth * cameraHeight];
    }

    @Override
    public void run() {
        YUV_NV21_TO_RGB(rgbFrame, yuvFrame, cameraWidth, cameraHeight);
        Point currentFramesRedCenterOfMass = findRedCenterOfMass();
        if (currentFramesRedCenterOfMass.x > 0 && currentFramesRedCenterOfMass.y > 0) {
            openGLSurfaceView.addPointsToDraw(currentFramesRedCenterOfMass);
        }
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

        for (int x = 0; x < cameraWidth; x++) {
            for (int y = 0; y < cameraHeight; y++) {
                if (rgbFrame[y * cameraWidth + x] > 0) {
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
