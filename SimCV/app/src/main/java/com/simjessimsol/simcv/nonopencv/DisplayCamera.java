package com.simjessimsol.simcv.nonopencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;

public class DisplayCamera extends SurfaceView implements Callback, PreviewCallback {
    private static final String TAG = "com.simjessimsol.simcv";

    private Camera camera;
    private SurfaceHolder holder;

    private int[] rgbColors;
    private Bitmap bitmap;
    private int cameraWidth;
    private int cameraHeight;

    public DisplayCamera(Context context) {
        super(context);
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
                camera.setPreviewDisplay(holder);
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

    protected final Paint rectanglePaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(1000, 300, 2000, 600, rectanglePaint);
        
        Log.i(TAG, "testdasds");
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Canvas canvas = null;
        try {
            synchronized (this) {
                canvas = holder.lockCanvas(null);
                int canvasWidth = canvas.getWidth();
                int canvasHeight = canvas.getHeight();

                YUV_NV21_TO_RGB(rgbColors, data, cameraWidth, cameraHeight);
                canvas.drawBitmap(rgbColors, 0, cameraWidth, canvasWidth - ((cameraWidth + canvasWidth) >> 1), canvasHeight - ((cameraHeight + canvasHeight) >> 1), cameraWidth, cameraHeight, false, null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
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
