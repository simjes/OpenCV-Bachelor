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

    private int viewWidth;
    private int viewHeight;

    private ArrayList<VertexPoint> pointsToDraw;
    private Line line;

    public OpenGLSurfaceView(Context context) {
        super(context);

        pointsToDraw = new ArrayList<>();

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //TODO: kan brukes til aa slippe å tegne linjene for hvert bilde
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //This setting prevents the GLSurfaceView frame from being redrawn until you call requestRender().
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
    }

    public void addPointsToDraw(Point point) {
        VertexPoint vertexPoint = pointToVertexPoint(point);
        pointsToDraw.add(vertexPoint);
        Log.i(TAG, "nyeste point, x: " + point.x + ", y: " + point.y);
    }


    private VertexPoint pointToVertexPoint(Point point) {
        float x = (float) point.x / viewWidth;
        float y = (float) point.y / viewHeight;
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

    public void setViewPort(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        viewWidth = width;
        viewHeight = height;
        Log.i(TAG, "GLVIEW: width:" + width + ", height: " + height);

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
