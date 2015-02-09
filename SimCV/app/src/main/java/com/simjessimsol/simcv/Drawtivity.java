package com.simjessimsol.simcv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

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
    private Mat storeYellowPoints;
    private Mat storeBluePoints;
    private Mat drawingMat;

    private Scalar lowYellow = new Scalar(20, 100, 100);
    private Scalar highYellow = new Scalar(30, 255, 255);
    private Scalar lowBlue = new Scalar(100, 100, 100);
    private Scalar highBlue = new Scalar(110, 255, 255);

    static int yellowPosX = 0;
    static int yellowPosY = 0;
    static int bluePosX = 0;
    static int bluePosY = 0;

    /*public int lowhue = 0;
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
    private SeekBar valueHighSeekBar;*/


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

        /*hueLowSeekBar = (SeekBar) findViewById(R.id.hueLowSeekBar);
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
        });*/

        cameraView = (CameraBridgeViewBase) findViewById(R.id.drawcam);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCameraIndex(0);
        cameraView.setCvCameraViewListener(this);
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
        storeYellowPoints = new Mat(new Size(width, height), 1);
        storeBluePoints = new Mat(new Size(width, height), 1);
        drawingMat = new Mat(new Size(width, height), CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        hsvFrame.release();
        inOutFrame.release();
        storeYellowPoints.release();
        storeBluePoints.release();
        drawingMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        inOutFrame = inputFrame.rgba();
        Imgproc.cvtColor(inOutFrame, hsvFrame, Imgproc.COLOR_RGB2HSV);


        Core.inRange(hsvFrame, lowYellow, highYellow, storeYellowPoints);
        Core.inRange(hsvFrame, lowBlue, highBlue, storeBluePoints);
        //Imgproc.erode(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        //Imgproc.erode(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        //Imgproc.dilate(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));
        //Imgproc.dilate(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));
        //Imgproc.erode(storeBluePoints, storeBluePoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        //Imgproc.erode(storeBluePoints, storeBluePoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));


        //yellow
        Moments yellowMoments = Imgproc.moments(storeYellowPoints, true);
        double yellowMoment10 = yellowMoments.get_m10();
        double yellowMoment01 = yellowMoments.get_m01();
        double yellowArea = yellowMoments.get_m00();

        int yellowLastX = yellowPosX;
        int yellowLastY = yellowPosY;

        yellowPosX = (int) (yellowMoment10 / yellowArea);
        yellowPosY = (int) (yellowMoment01 / yellowArea);

        Log.i(TAG, "posx: " + yellowPosX + ", posy: " + yellowPosY + ", yellowLastx: " + yellowLastX + ", yellowLasty: " + yellowLastY);
        Log.i(TAG, "drawmat: " + drawingMat.size() + ", " + drawingMat.channels() + ", tmpframe: " + inOutFrame.size() + ", " + inOutFrame.channels());
        if (yellowLastX > 0 && yellowLastY > 0 && yellowPosX > 0 && yellowPosY > 0) {
            Core.line(drawingMat, new Point(yellowPosX, yellowPosY), new Point(yellowLastX, yellowLastY), new Scalar(255, 255, 0), 10);
        }

        //Blue
        Moments blueMoments = Imgproc.moments(storeBluePoints, true);
        double blueMoment10 = blueMoments.get_m10();
        double blueMoment01 = blueMoments.get_m01();
        double blueArea = blueMoments.get_m00();

        int blueLastX = bluePosX;
        int blueLastY = bluePosY;

        bluePosX = (int) (blueMoment10 / blueArea);
        bluePosY = (int) (blueMoment01 / blueArea);

        if (blueLastX > 0 && blueLastY > 0 && bluePosX > 0 && bluePosY > 0) {
            Core.line(drawingMat, new Point(bluePosX, bluePosY), new Point(blueLastX, blueLastY), new Scalar(0, 0, 255), 10);
        }

        Core.add(inOutFrame, drawingMat, inOutFrame);

        //return storeBluePoints;
        return inOutFrame;
    }

}
