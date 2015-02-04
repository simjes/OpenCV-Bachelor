package com.simjessimsol.simcv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
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
import org.opencv.objdetect.Objdetect;


public class Drawtivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private final static String TAG = "lol";

    private CameraBridgeViewBase cameraView;
    private Mat hsvFrame;
    private Mat tmpFrame;
    private Mat storeYellowPoints;
    private Mat drawingMat = null;

    public Scalar lowYellow = new Scalar(20, 100, 100);
    public Scalar highYellow = new Scalar(30, 255, 255);

    static int posX = 0;
    static int posY = 0;

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


    private BaseLoaderCallback loader = new BaseLoaderCallback(this) {
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
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, loader);
        } else {
            Log.d(TAG, "Found OpenCV lib in the package");
            loader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        hsvFrame = new Mat();
        tmpFrame = new Mat();
        storeYellowPoints = new Mat(new Size(width, height), 1);
        drawingMat = new Mat(new Size(width, height), CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        hsvFrame.release();
        tmpFrame.release();
        storeYellowPoints.release();
        drawingMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        tmpFrame = inputFrame.rgba();
        Imgproc.cvtColor(tmpFrame, hsvFrame, Imgproc.COLOR_RGB2HSV);


        Core.inRange(hsvFrame, lowYellow, highYellow, storeYellowPoints);
        //Imgproc.erode(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        //Imgproc.erode(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        //Imgproc.dilate(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));
        //Imgproc.dilate(storeYellowPoints, storeYellowPoints, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));


        Moments myMoments = Imgproc.moments(storeYellowPoints, true);
        double moment10 = myMoments.get_m10();
        double moment01 = myMoments.get_m01();

        double area = myMoments.get_m00();

        int lastx = posX;
        int lasty = posY;

        posX = (int) (moment10 / area);
        posY = (int) (moment01 / area);

        Log.i(TAG, "posx: " + posX + ", posy: " + posY + ", lastx: " + lastx + ", lasty: " + lasty);
        Log.i(TAG, "drawmat: " + drawingMat.size() + ", " + drawingMat.channels() + ", tmpframe: " + tmpFrame.size() + ", " + tmpFrame.channels());
        if (lastx > 0 && lasty > 0 && posX > 0 && posY > 0) {
            Core.line(drawingMat, new Point(posX, posY), new Point(lastx, lasty), new Scalar(255, 255, 0), 10);
        }


        Core.add(tmpFrame, drawingMat, tmpFrame);

        return tmpFrame;
    }

}
