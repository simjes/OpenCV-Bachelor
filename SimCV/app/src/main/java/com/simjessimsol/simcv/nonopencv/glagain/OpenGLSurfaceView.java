package com.simjessimsol.simcv.nonopencv.glagain;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera.*;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.simjessimsol.simcv.nonopencv.noncvdrawwithgl.DirectVideo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLSurfaceView extends GLSurfaceView implements Renderer {
    private final static String TAG = "opengltracker2";
    private MyCamera camera;
    private SurfaceTexture surfaceTexture;
    private DirectVideo directVideo;

    private int width;
    private int height;

    public OpenGLSurfaceView(Context context, MyCamera camera) {
        super(context);
        this.camera = camera;
        setEGLContextClientVersion(2);
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texture = createTexture();
        directVideo = new DirectVideo(texture);
        surfaceTexture = new SurfaceTexture(texture);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        camera.startCamera(surfaceTexture, this);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
    }

    ArrayList<Point> cords = new ArrayList<>();

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] mtx = new float[16];
        surfaceTexture.updateTexImage();
        surfaceTexture.getTransformMatrix(mtx);

        directVideo.draw();


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        //TODO: egen traad? readpixels seeeeeein
        //GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, byteBuffer);
        //int[] pixelsAsInts = new int[width * height];
        //byteBuffer.asIntBuffer().get(pixelsAsInts);
        /*Point redMassCenter = findMassCenterOfRed(pixelsAsInts);
        if (redMassCenter.x > 0 && redMassCenter.y > 0) {
            cords.add(redMassCenter);
        }*/
        //Log.d(TAG, "cords for rød, x: " + redMassCenter.x + ", y: " + redMassCenter.y);
    }

    private Point findMassCenterOfRed(int[] pixels) {
        Point avrage = new Point(0, 0);
        int nrOfPoints = 0;
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

        return avrage;
    }

    //TODO: getting wrong resolution
    public int[] findOptimalResolution(Parameters parameters) {
        List<Camera.Size> supportedCameraSizes = parameters.getSupportedPreviewSizes();
        int optimalWidth = 0;
        int optimalHeight = 0;

        for (Camera.Size s : supportedCameraSizes) {
            Log.d(TAG, "optimal, size width: " + s.width + ", size height: " + s.height);
            //TODO: feil resolution, pga glSurfaceView.getWidth()/height?
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


    //TODO: copied from http://stackoverflow.com/questions/19852680/android-opengl-camera-preview-issue
    private int createTexture() {
        int[] textures = new int[1];

        // generate one texture pointer and bind it as an external texture.
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);

        // No mip-mapping with camera source.
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Clamp to edge is only option.
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return textures[0];
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}