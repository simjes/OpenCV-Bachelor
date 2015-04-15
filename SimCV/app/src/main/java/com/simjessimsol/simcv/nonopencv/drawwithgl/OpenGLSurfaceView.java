package com.simjessimsol.simcv.nonopencv.drawwithgl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLSurfaceView extends GLSurfaceView implements Renderer {
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

    public void addPointsToDraw(VertexPoint vertexPoint) {
        pointsToDraw.add(vertexPoint);
    }

    public void setViewPort(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
