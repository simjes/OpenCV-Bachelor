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

    //private int viewWidth;
    //private int viewHeight;

    private ArrayList<VertexPoint> pointsToDraw;
    private Line line;

    public OpenGLSurfaceView(Context context) {
        super(context);

        pointsToDraw = new ArrayList<>();

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setRenderer(this);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        line = new Line();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }


    //TODO: adde til surfaceviewen, istedet for å tegne på ny hver gang ?

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (pointsToDraw.size() > 2) {
            for (int i = 1; i < pointsToDraw.size(); i++) {
                VertexPoint lastPoint = pointsToDraw.get(i - 1);
                VertexPoint currentPoint = pointsToDraw.get(i);

                line.setCoords(lastPoint.getX(), lastPoint.getY(), currentPoint.getX(), currentPoint.getY());
                line.draw();
            }
        }
        Log.d(TAG, "glsurfaceview hw acc: " + isHardwareAccelerated());
    }

    public void addPointsToDraw(VertexPoint vertexPoint) {
        pointsToDraw.add(vertexPoint);
    }

    public void setViewPort(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //viewWidth = width;
        //viewHeight = height;
        //Log.i(TAG, "GLVIEW: width:" + width + ", height: " + height);

        //Matrix.setLookAtM(mtx, 0, 0, 0, 1.5f, 0, 0, -5f, 0, 0, 0);
    }

    //TODO: http://developer.android.com/training/graphics/opengl/draw.html
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
