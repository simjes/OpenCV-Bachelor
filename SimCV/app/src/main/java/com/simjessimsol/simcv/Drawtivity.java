package com.simjessimsol.simcv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


public class Drawtivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private final static String TAG = "com.simjessimsol.simcv";

    private CameraBridgeViewBase cameraView;
    private Mat hsvFrame;
    private Mat inOutFrame;
    private Mat storeRedPoints;
    private Mat storeBluePoints;
    private Mat storeGreenPoints;
    private Mat drawingMat;

    private boolean paused = true;
    private ImageButton pauseButton;

    private Scalar lowRed = new Scalar(163, 191, 211);
    private Scalar highRed = new Scalar(180, 255, 255);
    private Scalar lowBlue = new Scalar(87, 200, 185);
    private Scalar highBlue = new Scalar(104, 255, 255);
    private Scalar lowGreen = new Scalar(38, 108, 125);
    private Scalar highGreen = new Scalar(73, 255, 255);

    private static int redPosX = 0;
    private static int redPosY = 0;
    private static int bluePosX = 0;
    private static int bluePosY = 0;
    private static int greenPosX = 0;
    private static int greenPosY = 0;

    /*
    public int lowhue = 0;
    public int lowsat = 0;
    public int lowval = 0;
    public int highhue = 0;
    public int highsat = 0;
    public int highval = 0;
    private SeekBar hueLowSeekBar;
    private SeekBar saturationLowSeekBar;
    private SeekBar valueLowSeekBar;
    private SeekBar hueHighSeekBar;
    private SeekBar saturationHighSeekBar;
    private SeekBar valueHighSeekBar;
    */
    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    cameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_drawtivity);

        pauseButton = (ImageButton) findViewById(R.id.startDrawingButton);

        /*
        hueLowSeekBar = (SeekBar) findViewById(R.id.hueLowSeekBar);
        saturationLowSeekBar = (SeekBar) findViewById(R.id.saturationLowSeekBar);
        valueLowSeekBar = (SeekBar) findViewById(R.id.valueLowSeekBar);
        hueHighSeekBar = (SeekBar) findViewById(R.id.hueHighSeekBar);
        saturationHighSeekBar = (SeekBar) findViewById(R.id.saturationHighSeekBar);
        valueHighSeekBar = (SeekBar) findViewById(R.id.valueHighSeekBar);
        hueLowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lowhue = progressVal;
            }
        });
        saturationLowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lowsat = progressVal;
            }
        });
        valueLowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lowval = progressVal;
            }
        });
        hueHighSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                highhue = progressVal;
            }
        });
        saturationHighSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                highsat = progressVal;
            }
        });
        valueHighSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                highval = progressVal;
            }
        });
        */

        cameraView = (CameraBridgeViewBase) findViewById(R.id.drawcam);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCameraIndex(0);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        if (cameraView != null) {
            cameraView.disableView();
        }
        paused = true;
        pauseButton.setImageResource(R.drawable.ic_action_play);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV Manager used");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, loaderCallback);
        } else {
            Log.d(TAG, "Found OpenCV lib in the package");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        hsvFrame = new Mat();
        inOutFrame = new Mat();
        storeRedPoints = new Mat(new Size(width, height), 1);
        storeBluePoints = new Mat(new Size(width, height), 1);
        storeGreenPoints = new Mat(new Size(width, height), 1);
        drawingMat = new Mat(new Size(width, height), CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        hsvFrame.release();
        inOutFrame.release();
        storeRedPoints.release();
        storeBluePoints.release();
        drawingMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        inOutFrame = inputFrame.rgba();

        if (!paused) {
            int redLastX, redLastY, blueLastX, blueLastY, greenLastX, greenLastY;
            Imgproc.cvtColor(inOutFrame, hsvFrame, Imgproc.COLOR_RGB2HSV);

            Core.inRange(hsvFrame, lowRed, highRed, storeRedPoints);
            Core.inRange(hsvFrame, lowBlue, highBlue, storeBluePoints);
            Core.inRange(hsvFrame, lowGreen, highGreen, storeGreenPoints);
            //Imgproc.erode(storeRedPoints, storeRedPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            //Imgproc.erode(storeRedPoints, storeRedPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            //Imgproc.dilate(storeRedPoints, storeRedPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));
            //Imgproc.dilate(storeRedPoints, storeRedPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));
            //Imgproc.erode(storeBluePoints, storeBluePoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            //Imgproc.erode(storeBluePoints, storeBluePoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));


            //red
            Moments redMoments = Imgproc.moments(storeRedPoints, true);
            double redMoment10 = redMoments.get_m10();
            double redMoment01 = redMoments.get_m01();
            double redArea = redMoments.get_m00();

            redLastX = redPosX;
            redLastY = redPosY;

            redPosX = (int) (redMoment10 / redArea);
            redPosY = (int) (redMoment01 / redArea);

            if (redLastX > 0 && redLastY > 0 && redPosX > 0 && redPosY > 0) {
                Core.line(drawingMat, new Point(redPosX, redPosY), new Point(redLastX, redLastY), new Scalar(255, 0, 0), 10);
            }

            //Blue
            Moments blueMoments = Imgproc.moments(storeBluePoints, true);
            double blueMoment10 = blueMoments.get_m10();
            double blueMoment01 = blueMoments.get_m01();
            double blueArea = blueMoments.get_m00();

            blueLastX = bluePosX;
            blueLastY = bluePosY;

            bluePosX = (int) (blueMoment10 / blueArea);
            bluePosY = (int) (blueMoment01 / blueArea);

            if (blueLastX > 0 && blueLastY > 0 && bluePosX > 0 && bluePosY > 0) {
                Core.line(drawingMat, new Point(bluePosX, bluePosY), new Point(blueLastX, blueLastY), new Scalar(0, 0, 255), 10);
            }

            //Green
            Moments greenMoments = Imgproc.moments(storeGreenPoints, true);
            double greenMoment10 = greenMoments.get_m10();
            double greenMoment01 = greenMoments.get_m01();
            double greenArea = greenMoments.get_m00();

            greenLastX = greenPosX;
            greenLastY = greenPosY;

            greenPosX = (int) (greenMoment10 / greenArea);
            greenPosY = (int) (greenMoment01 / greenArea);
            if (greenPosX > 0 && greenPosY > 0 && greenLastX > 0 && greenLastY > 0) {
                Core.line(drawingMat, new Point(greenPosX, greenPosY), new Point(greenLastX, greenLastY), new Scalar(0, 255, 0), 10);
            }
        } else {
            redPosX = 0;
            redPosY = 0;
            greenPosX = 0;
            greenPosY = 0;
            bluePosX = 0;
            bluePosY = 0;
        }
        Core.add(inOutFrame, drawingMat, inOutFrame);
        //return storeRedPoints;
        return inOutFrame;

        /*Core.inRange(hsvFrame, new Scalar(lowhue, lowsat, lowval), new Scalar(highhue, highsat, highval), hsvFrame);
        Log.i(TAG, "lowHue: " + lowhue + ", lowSat: " + lowsat + ", lowVal: " + lowval + "....highHue: " + highhue + ", highSat: " + highsat + ", highVal: " + highval);
        return hsvFrame;*/
    }

    public void onPauseClick(View view) {
        if (paused) {
            paused = false;
            pauseButton.setImageResource(R.drawable.ic_action_pause);
        } else {
            paused = true;
            pauseButton.setImageResource(R.drawable.ic_action_play);

        }
    }

}
