package com.simjessimsol.simcv.nonopencv.opengltracker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private DirectVideo directVideo;
    private int texture;
    private SurfaceTexture surfaceTexture;
    private OpenGLTracker mainActivity;

    public OpenGLRenderer(OpenGLTracker context) {
        this.mainActivity = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        texture = createTexture();
        directVideo = new DirectVideo(texture);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        mainActivity.startCamera(texture);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
    }

    //TODO: fjern
    private int width;
    private int height;
    private Bitmap lastScreenshot;
    private int counter = 0;
    private final static String TAG = "lel";

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        surfaceTexture.updateTexImage();
        surfaceTexture.getTransformMatrix(mtx);

        directVideo.draw();

        /*//TODO: fjern
        counter++;
        Log.d(TAG, "counter: " + counter);

        if (counter == 100) {
            int screenshotSize = width * height;
            ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
            bb.order(ByteOrder.nativeOrder());
            GLES20.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
            int pixelsBuffer[] = new int[screenshotSize];
            bb.asIntBuffer().get(pixelsBuffer);
            bb = null;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0, width, height);
            pixelsBuffer = null;

            short sBuffer[] = new short[screenshotSize];
            ShortBuffer sb = ShortBuffer.wrap(sBuffer);
            bitmap.copyPixelsToBuffer(sb);

            //Making created bitmap (from OpenGL points) compatible with Android bitmap
            for (int i = 0; i < screenshotSize; ++i) {
                short v = sBuffer[i];
                sBuffer[i] = (short) (((v & 0x1f) << 11) | (v & 0x7e0) | ((v & 0xf800) >> 11));
            }
            sb.rewind();
            bitmap.copyPixelsFromBuffer(sb);
            lastScreenshot = bitmap;
            //TODO:stuff
            Date first = new Date();
            int[] pixels = new int[width * height];
            lastScreenshot.getPixels(pixels, 0, width, 0, 0, width, height);

            for (int i = 0; i < pixels.length; i++) {
                int pixelVal = pixels[i];
                if (Color.red(pixelVal) >= 200 && Color.green(pixelVal) <= 45 && Color.blue(pixelVal) <= 45) {
                    pixels[i] = 0xffffff;
                } else {
                    pixels[i] = 0;
                }
            }

            //lastScreenshot.setPixels(pixels, 0, width, 0, 0, width, height);
            Point avg = new Point(0, 0);
            int nrOfPoints = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (pixels[y * width + x] > 0) {
                        avg.x += x;
                        avg.y += y;
                        nrOfPoints++;
                    }
                }
            }

            if (nrOfPoints > 0) {
                Log.d(TAG, "x mass center: " + (avg.x / nrOfPoints) + ", y mass center: " + (avg.y / nrOfPoints));
                Log.d(TAG, "widht: " + lastScreenshot.getWidth() + ", height: " + lastScreenshot.getHeight());
                Log.d(TAG, "slutt: " + (new Date().getTime() - first.getTime()));
                int x = avg.x / nrOfPoints;
                int y = avg.y / nrOfPoints;
                lastScreenshot.setPixel(x, y, Color.BLACK);
                lastScreenshot.setPixel(x - 1, y, Color.BLUE);
                lastScreenshot.setPixel(x - 2, y, Color.BLUE);
                lastScreenshot.setPixel(x - 3, y, Color.BLUE);
                lastScreenshot.setPixel(x + 1, y, Color.BLUE);
                lastScreenshot.setPixel(x + 2, y, Color.BLUE);
                lastScreenshot.setPixel(x + 3, y, Color.BLUE);
                lastScreenshot.setPixel(x, y + 1, Color.BLUE);
                lastScreenshot.setPixel(x, y - 1, Color.BLUE);
            }


            //TODO: end stuff
            openGLTrackerActivity.saveBitmap(lastScreenshot);*/
        //}
    }

    static public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static private int createTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }
}
