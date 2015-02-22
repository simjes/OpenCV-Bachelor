package com.simjessimsol.simcv.nonopencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class DisplayCamera extends SurfaceView implements Callback, PreviewCallback {
    private static final String TAG = "com.simjessimsol.simcv";

    private Camera camera;
    private SurfaceHolder holder;

    private int[] rgbColors;
    private Bitmap bitmap;
    private int cameraWidth;
    private int cameraHeight;

    private Paint rectanglePaint = new Paint();

    public DisplayCamera(Context context) {
        super(context);
        rectanglePaint.setColor(Color.RED);
        rectanglePaint.setStrokeWidth(10);

        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            this.setWillNotDraw(false);

            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            cameraWidth = parameters.getPreviewSize().width;
            cameraHeight = parameters.getPreviewSize().height;

            rgbColors = new int[cameraWidth * cameraHeight];


            try {
                camera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
            camera.setPreviewCallback(this);

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            try {
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /*@Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(100, 100, 200, 200, rectanglePaint);

        Log.i(TAG, "called onDraw");
    }*/

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d(TAG, "getting frame");

        YUV_NV21_TO_RGB(rgbColors, data, cameraWidth, cameraHeight);

        bitmap = Bitmap.createBitmap(rgbColors, cameraWidth, cameraHeight, Bitmap.Config.RGB_565);
        if (centerOfmas != null) {
            lastPoint = centerOfmas;
        }
        centerOfmas = findRedCenterOfMas(bitmap);
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(bitmap, (canvas.getWidth() - cameraWidth) / 2, (canvas.getHeight() - cameraHeight) / 2, null);
            if (lastPoint != null && centerOfmas != null) {
                canvas.drawLine(lastPoint.x, lastPoint.y, centerOfmas.x, centerOfmas.y, rectanglePaint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
        bitmap.recycle();
    }

    private Point centerOfmas;
    private Point lastPoint;

    private Point findRedCenterOfMas(Bitmap bm) {
        Date first = new Date();
        Log.d(TAG, "starta find mass center: " + first);
        Point avrage = new Point(0, 0);
        int nrOfPoints = 0;

        for (int rows = 0; rows < bm.getWidth(); rows++) {
            for (int cols = 0; cols < bm.getHeight(); cols++) {
                int redVal = Color.red(bm.getPixel(rows, cols));
                int greenVal = Color.green(bm.getPixel(rows, cols));
                int blueVal = Color.blue(bm.getPixel(rows, cols));
                if (redVal >= 200 && greenVal <= 45 && blueVal <= 45) {
                    avrage.x += rows;
                    avrage.y += cols;
                    nrOfPoints++;
                }
            }
        }

        avrage.x /= nrOfPoints;
        avrage.y /= nrOfPoints;


        Log.d(TAG, "slutt: " + (new Date().getTime() - first.getTime()));
        return avrage;
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
