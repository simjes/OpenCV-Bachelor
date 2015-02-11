package com.simjessimsol.simcv;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

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
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.File;


public class Drawtivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private final static String TAG = "com.simjessimsol.simcv";
    private static final String PHOTO_MIME_TYPE = "image/png";

    private CameraBridgeViewBase cameraView;
    private Mat hsvFrame;
    private Mat inOutFrame;
    private Mat storeRedPoints;
    private Mat storeBluePoints;
    private Mat storeGreenPoints;
    private Mat drawingMat;
    private Mat pictureToSave;

    private boolean paused = true;
    private ImageButton pauseButton;
    private boolean takePhotoClicked = false;

    private Scalar lowRed = new Scalar(163, 191, 211);
    private Scalar highRed = new Scalar(180, 255, 255);
    private Scalar lowBlue = new Scalar(64, 189, 119);
    private Scalar highBlue = new Scalar(106, 255, 198);
    private Scalar lowGreen = new Scalar(38, 108, 125);
    private Scalar highGreen = new Scalar(73, 255, 255);

    private static int redPosX = 0;
    private static int redPosY = 0;
    private static int bluePosX = 0;
    private static int bluePosY = 0;
    private static int greenPosX = 0;
    private static int greenPosY = 0;

    private Scalar colorToDrawFromRed = new Scalar(255, 0, 0, 255);
    private Scalar colorToDrawFromGreen = new Scalar(0, 255, 0, 255);
    private Scalar colorToDrawFromBlue = new Scalar(0, 0, 255, 255);


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

        pauseButton = (ImageButton) findViewById(R.id.startDrawingButton);

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
        pictureToSave = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        hsvFrame.release();
        inOutFrame.release();
        storeRedPoints.release();
        storeBluePoints.release();
        storeGreenPoints.release();
        drawingMat.release();
        pictureToSave.release();
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
                Core.line(drawingMat, new Point(redPosX, redPosY), new Point(redLastX, redLastY), colorToDrawFromRed, 10);
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
                Core.line(drawingMat, new Point(bluePosX, bluePosY), new Point(blueLastX, blueLastY), colorToDrawFromBlue, 10);
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
                Core.line(drawingMat, new Point(greenPosX, greenPosY), new Point(greenLastX, greenLastY), colorToDrawFromGreen, 10);
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

        if (takePhotoClicked) {
            takePhoto(inOutFrame);
            takePhotoClicked = false;
        }
        //return storeBluePoints;
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

    public void onClearDrawingClick(View view) {
        drawingMat = new Mat(inOutFrame.size(), CvType.CV_8UC4);
        paused = true;
        pauseButton.setImageResource(R.drawable.ic_action_play);
    }

    public void onTakePictureClick(View view) {
        takePhotoClicked = true;
        Toast.makeText(this, "Photo Saved", Toast.LENGTH_SHORT).show();
    }

    /*private void findDrawColors(Mat mat) {
        Moments Moments = Imgproc.moments(mat, true);
        double redMoment10 = Moments.get_m10();
        double redMoment01 = Moments.get_m01();
        double redArea = Moments.get_m00();

        redLastX = redPosX;
        redLastY = redPosY;

        redPosX = (int) (redMoment10 / redArea);
        redPosY = (int) (redMoment01 / redArea);

        if (redLastX > 0 && redLastY > 0 && redPosX > 0 && redPosY > 0) {
            Core.line(drawingMat, new Point(redPosX, redPosY), new Point(redLastX, redLastY), colorToDrawFromRed, 10);
        }
    }*/

    //TODO: Copied from Android Application Programming with OpenCV.pdf
    private void takePhoto(Mat mat) {
        long currentTimeInMilliSec = System.currentTimeMillis();
        String appName = getString(R.string.app_name);
        String galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String albumPath = galleryPath + "/" + appName;
        String photoPath = albumPath + "/" + currentTimeInMilliSec + ".png";
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, photoPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, PHOTO_MIME_TYPE);
        values.put(MediaStore.Images.Media.TITLE, appName);
        values.put(MediaStore.Images.Media.DESCRIPTION, appName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeInMilliSec);

        File album = new File(albumPath);
        if (!album.isDirectory() && !album.mkdirs()) {
            Log.e(TAG, "Failed to create album directory at " +
                    albumPath);
            return;
        }

        Imgproc.cvtColor(mat, pictureToSave, Imgproc.COLOR_RGBA2BGR, 3);
        if (!Highgui.imwrite(photoPath, pictureToSave)) {
            Log.e(TAG, "Failed to save photo to " + photoPath);
        }
        Log.d(TAG, "Photo saved successfully to " + photoPath);

        Uri uri;

        try {
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (final Exception e) {
            Log.e(TAG, "Failed to insert photo into MediaStore");
            e.printStackTrace();
            // Since the insertion failed, delete the photo.
            File photo = new File(photoPath);
            if (!photo.delete()) {
                Log.e(TAG, "Failed to delete non-inserted photo");
            }
            return;
        }
    }
}
