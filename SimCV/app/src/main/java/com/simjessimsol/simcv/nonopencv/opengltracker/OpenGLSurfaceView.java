package com.simjessimsol.simcv.nonopencv.opengltracker;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class OpenGLSurfaceView extends GLSurfaceView {
    private OpenGLRenderer renderer;

    public OpenGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        renderer = new OpenGLRenderer((OpenGLTracker) context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public OpenGLRenderer getRenderer() {
        return renderer;
    }
}
