package com.simjessimsol.simcv;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LauncherActivity extends Activity implements CvCameraViewListener2 {

    private CameraBridgeViewBase cameraView;
    private final static String TAG = "com.simjessimsol.simcv";

    private final static String STATE_CAMERA_INDEX = "cameraIndex";
    private final static String STATE_TRACKING_FILTER = "trackingFilter";

    private int cameraIndex;
    private String trackingFilter;
    private boolean isCameraFrontFacing;
    private int numberOfCameras;

    private Mat inputFrame;

    //Face detection
    private Mat grayscaleImg;
    private Mat faceDetectedImage;
    private File cascadeFile;
    private CascadeClassifier detector;
    private final int SCALE = 2;


    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(TAG, "OpenCV loaded successfully");
                    try {
                        InputStream inputStream = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("haarcascade", Context.MODE_PRIVATE);
                        cascadeFile = new File(cascadeDir, "haarcascade_face.xml");
                        FileOutputStream outputStream = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();

                        detector = new CascadeClassifier(cascadeFile.getAbsolutePath());

                        cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Cascade failed to load: " + e);
                    }
                    cameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_launcher);

        if (savedInstanceState != null) {
            cameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
            trackingFilter = savedInstanceState.getString(STATE_TRACKING_FILTER);
        } else {
            cameraIndex = 0;
            trackingFilter = "none";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            isCameraFrontFacing = (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT);
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
        outState.putString(STATE_TRACKING_FILTER, trackingFilter);
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
            Log.d(TAG, "Found OpenCv lib in the package");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inFrame) {
        inputFrame = inFrame.rgba();
        if (isCameraFrontFacing) {
            Core.flip(inputFrame, inputFrame, 1);
        }
        switch (trackingFilter) {
            case "none":
                return inputFrame;
            case "detectFace":
                faceDetectedImage = findFaces(inputFrame);
                return faceDetectedImage;
            default:
                return inputFrame;
        }
    }

    public void changeCameraClick(View view) {
        if (cameraIndex == 0) {
            cameraIndex = 1;
            cameraView.setCameraIndex(cameraIndex);
        } else {
            cameraIndex = 0;
            cameraView.setCameraIndex(cameraIndex);
        }
        recreate();
    }

    public void detectFaceClick(View view) {
        if (trackingFilter.equals("detectFace")) {
            trackingFilter = "none";
            Toast.makeText(this, "No filter", Toast.LENGTH_SHORT).show();
        } else {
            trackingFilter = "detectFace";
            Toast.makeText(this, "Face detection", Toast.LENGTH_SHORT).show();
        }
    }

    private Mat findFaces(Mat originalImage) {
        grayscaleImg = new Mat();

        Imgproc.cvtColor(originalImage, grayscaleImg, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.resize(grayscaleImg, grayscaleImg, new Size(originalImage.size().width / SCALE, originalImage.size().height / SCALE));
        Imgproc.equalizeHist(grayscaleImg, grayscaleImg);

        MatOfRect detectedFaces = new MatOfRect();
        detector.detectMultiScale(grayscaleImg, detectedFaces);

        for (Rect r : detectedFaces.toArray()) {
            Core.rectangle(originalImage, new Point(r.x * SCALE, r.y * SCALE), new Point((r.x + r.width) * SCALE, (r.y + r.height) * SCALE), new Scalar(0, 0, 255), 3);
        }

        return originalImage;
    }
}
