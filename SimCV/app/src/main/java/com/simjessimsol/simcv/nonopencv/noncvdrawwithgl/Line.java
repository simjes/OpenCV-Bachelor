package com.simjessimsol.simcv.nonopencv.noncvdrawwithgl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Line {
    private FloatBuffer vertexBuffer;
    private final int program;
    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = lineCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private static final int COORDS_PER_VERTEX = 3;
    private static float[] lineCoords = {
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

    private float[] color = {1.0f, 0.0f, 0.0f, 1.0f};

    private final String vertexShaderCode = "" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}";

    private final String fragmentShaderCode = "" +
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    public Line() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(lineCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(lineCoords);
        vertexBuffer.position(0);

        int vertexShader = OpenGLSurfaceView.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OpenGLSurfaceView.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void setCoords(float xStart, float yStart, float xEnd, float yEnd) {
        lineCoords[0] = xStart;
        lineCoords[1] = yStart;
        lineCoords[3] = xEnd;
        lineCoords[4] = yEnd;

        vertexBuffer.put(lineCoords);
        vertexBuffer.position(0);
    }

    public void draw() {
        GLES20.glUseProgram(program);
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
