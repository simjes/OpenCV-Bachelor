package com.simjessimsol.simcv.detection;

import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.simjessimsol.simcv.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG;

import java.io.File;


public class ForegroundDetection extends Activity implements CvCameraViewListener2 {

    private final static String TAG = "detection";
    private final static String STATE_CAMERA_INDEX = "cameraIndex";

    private CameraBridgeViewBase cameraView;
    private int cameraIndex;
    private boolean isCameraFrontFacing;


    private Mat inOutFrame;
    private Mat foregroundImage;

    private BackgroundSubtractorMOG backgroundSubtractorMog;

    //TODO: DEBUG STUFF
    private Mat pictureToSave;
    private boolean takePhotoClicked;

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
        setContentView(R.layout.activity_foregrounddetection);

        if (savedInstanceState != null) {
            cameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
        } else {
            cameraIndex = 0;
        }

        int numberOfCameras;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            isCameraFrontFacing = (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
            numberOfCameras = Camera.getNumberOfCameras();
        } else {
            isCameraFrontFacing = false;
            numberOfCameras = 1;
        }

        if (numberOfCameras < 2) {
            ImageButton changeCameraButton = (ImageButton) findViewById(R.id.changeCameraButton);
            changeCameraButton.setVisibility(View.GONE);
        }

        cameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCVCamView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCameraIndex(cameraIndex);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CAMERA_INDEX, cameraIndex);
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
        inOutFrame = new Mat();
        pictureToSave = new Mat();
        foregroundImage = new Mat();
        backgroundSubtractorMog = new BackgroundSubtractorMOG();
    }

    @Override
    public void onCameraViewStopped() {
        inOutFrame.release();
        pictureToSave.release();
        foregroundImage.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        inOutFrame = inputFrame.rgba();
        if (isCameraFrontFacing) {
            Core.flip(inOutFrame, inOutFrame, 1);
        }

        backgroundSubtractorMog.apply(inputFrame.gray(), foregroundImage);

        //Imgproc.erode(foregroundImage, foregroundImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9)));
        //Imgproc.erode(foregroundImage, foregroundImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9)));

        if (takePhotoClicked) {
            takePhoto(inOutFrame);
            takePhotoClicked = false;
        }
        return foregroundImage;
    }

    public void changeCameraClick(View view) {
        cameraView.disableView();
        cameraIndex ^= 1;
        cameraView.setCameraIndex(cameraIndex);
        cameraView.enableView();
    }

    public void onTakePictureClick(View view) {
        takePhotoClicked = true;
        Toast.makeText(this, "Photo Saved", Toast.LENGTH_SHORT).show();
    }

    //TODO: Copied from Android Application Programming with OpenCV.pdf
    private void takePhoto(Mat mat) {
        long currentTimeInMilliSec = System.currentTimeMillis();
        String appName = getString(R.string.app_name);
        String galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String albumPath = galleryPath + "/" + appName;
        String photoPath = albumPath + "/" + currentTimeInMilliSec + ".png";
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, photoPath);
        values.put(MediaStore.Images.Media.TITLE, appName);
        values.put(MediaStore.Images.Media.DESCRIPTION, appName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeInMilliSec);

        File album = new File(albumPath);
        if (!album.isDirectory() && !album.mkdirs()) {
            Log.e(TAG, "Failed to create album directory at " +
                    albumPath);
            return;
        }

        Imgproc.cvtColor(mat, pictureToSave, Imgproc.COLOR_HSV2BGR, 3);
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
        }
    }
}
