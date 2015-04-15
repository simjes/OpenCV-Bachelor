package com.simjessimsol.simcv;

import android.util.Log;


public class Performance {

    private final static String TAG = "performance";

    private long startTime;
    private long lastTime;

    private int nrOfMilliSec;
    private float avgMilliSek;

    private int frames;

    public Performance() {
        frames = 0;
        nrOfMilliSec = 0;
        avgMilliSek = 0;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        Log.d(TAG, "Time to handle frame: nanoseconds:" + timeElapsed + ", microseconds: " + timeElapsed / 1000 + ", milliseconds: " + timeElapsed / 1000000);
        avgMilliSek += (timeElapsed / 1000000);
        nrOfMilliSec++;
        if (nrOfMilliSec > 20) {
            Log.d(TAG, "Average time in millisec: " + avgMilliSek / nrOfMilliSec);
            nrOfMilliSec = 0;
            avgMilliSek = 0;
        }
    }

    public void startFPSCounter() {
        lastTime = System.nanoTime() / 1000000000;
    }

    public void addFrame() {
        long currentTime = System.nanoTime() / 1000000000;
        frames++;

        if (currentTime - lastTime >= 1.0) {
            Log.d(TAG, "FPS: " + frames);
            frames = 0;
            lastTime += 1.0;
        }

    }

}
