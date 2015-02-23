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


    //TODO: Fungerer kun på android build < 4.4(?)
    //TODO: sett lik resolution som OpenCV
    //TODO: sjekk fps
    private Camera camera;
    private SurfaceHolder holder;

    private int[] rgbColors;
    private Bitmap bitmap;
    private int cameraWidth;
    private int cameraHeight;

    private Paint rectanglePaint = new Paint();


    private Point centerOfmass;
    private ArrayList<Point> pointsOfMass;

    public DisplayCamera(Context context) {
        super(context);
        pointsOfMass = new ArrayList<>();
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
        Log.d(TAG, "width: " + bitmap.getWidth() + ", height: " + bitmap.getHeight() + ", camWidth: " + cameraWidth + ", camHeight: " + cameraHeight);

        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {
            int centerCanvasWidthHelper = (canvas.getWidth() - cameraWidth) / 2;
            int centerCavnasHeightHelper = (canvas.getHeight() - cameraHeight) / 2;
            centerOfmass = findRedCenterOfMas(bitmap);
            centerOfmass.x += centerCanvasWidthHelper;
            centerOfmass.y += centerCavnasHeightHelper;
            pointsOfMass.add(centerOfmass);


            canvas.drawBitmap(bitmap, centerCanvasWidthHelper, centerCavnasHeightHelper, null);
            Log.d(TAG, "canvas width: " + canvas.getWidth() + ", canvasHeight: " + canvas.getHeight());
            if (pointsOfMass.size() > 2) {
                for (int i = 1; i < pointsOfMass.size(); i++) {
                    Point lastPoint = pointsOfMass.get(i - 1);
                    Point thisPoint = pointsOfMass.get(i);
                    if (lastPoint.x > 0 && lastPoint.y > 0 && thisPoint.x > 0 && thisPoint.y > 0) {

                        canvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, rectanglePaint);

                    }
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }
        bitmap.recycle();
    }

    private Point findRedCenterOfMas(Bitmap bm) {
        Date first = new Date();
        Point avrage = new Point(0, 0);
        int nrOfPoints = 0;
        int width = bm.getWidth();
        int height = bm.getHeight();
        int[] pixels = new int[width * height];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int pixelVal = pixels[i];
            if (Color.red(pixelVal) >= 200 && Color.green(pixelVal) <= 45 && Color.blue(pixelVal) <= 45) {
                pixels[i] = 0xffffff;
            } else {
                pixels[i] = 0;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (pixels[y * width + x] > 0) {
                    avrage.x += x;
                    avrage.y += y;
                    nrOfPoints++;
                }
            }
        }
        if (nrOfPoints > 0) {
            avrage.x /= nrOfPoints;
            avrage.y /= nrOfPoints;
        }
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