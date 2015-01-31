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
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LauncherActivity extends Activity implements CvCameraViewListener2 {

    private final static String TAG = "com.simjessimsol.simcv";

    private final static String STATE_CAMERA_INDEX = "cameraIndex";
    private final static String STATE_TRACKING_FILTER = "trackingFilter";
    private final static String STATE_NATIVE_OR_JAVA = "nativeOrJava";

    private CameraBridgeViewBase cameraView;
    private int cameraIndex;
    private String trackingFilter;
    private boolean isCameraFrontFacing;
    private int numberOfCameras;
    private String nativeOrJava;

    private Mat inputFrame;
    private Mat detectedImage;
    private File cascadeFile;
    private CascadeClassifier detector;
    private final int SCALE = 3;


    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(TAG, "OpenCV loaded successfully");
                    System.loadLibrary("nativeDetection");
                    try {
                        InputStream inputStream = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("lbpcascade", Context.MODE_PRIVATE);
                        cascadeFile = new File(cascadeDir, "lbpcascade_face.xml");
                        FileOutputStream outputStream = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();

                        detector = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        NativeDetection.sendCascadeFile(cascadeFile.getAbsolutePath());

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
            nativeOrJava = savedInstanceState.getString(STATE_NATIVE_OR_JAVA);
        } else {
            cameraIndex = 0;
            trackingFilter = "none";
            nativeOrJava = "java";
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageButton nativeJavaSwitch = (ImageButton) findViewById(R.id.nativeJavaSwitch);
            nativeJavaSwitch.setVisibility(View.GONE);
        }

        if (numberOfCameras < 2) {
            ImageButton changeCameraButton = (ImageButton) findViewById(R.id.changeCameraButton);
            changeCameraButton.setVisibility(View.GONE);
        }

        if (nativeOrJava.equals("java")) {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCVCamView);
        } else {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.NativeOpenCVCamView);
        }
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCameraIndex(cameraIndex);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CAMERA_INDEX, cameraIndex);
        outState.putString(STATE_TRACKING_FILTER, trackingFilter);
        outState.putString(STATE_NATIVE_OR_JAVA, nativeOrJava);
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
        inputFrame = new Mat();
        detectedImage = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        inputFrame.release();
        detectedImage.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inFrame) {
        inputFrame = inFrame.rgba();
        if (isCameraFrontFacing) {
            Core.flip(inputFrame, inputFrame, 1);
        }
        if (nativeOrJava.equals("java")) {
            switch (trackingFilter) {
                case "detectFace":
                    detectedImage = JavaDetection.findFaces(inputFrame, SCALE, detector);
                    return detectedImage;
                case "detectCircle":
                    detectedImage = JavaDetection.findCircle(inputFrame, SCALE);
                    return detectedImage;
                default:
                    return inputFrame;
            }
        } else {
            switch (trackingFilter) {
                case "detectFace":
                    NativeDetection.nativeDetectFace(inputFrame.getNativeObjAddr());
                    return inputFrame;
                default:
                    return inputFrame;
            }
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

    public void detectCircleClick(View view) {
        if (trackingFilter.equals("detectCircle")) {
            trackingFilter = "none";
            Toast.makeText(this, "No filter", Toast.LENGTH_SHORT).show();
        } else {
            trackingFilter = "detectCircle";
            Toast.makeText(this, "Circle detection", Toast.LENGTH_SHORT).show();
        }
    }

    public void nativeJavaSwitchClick(View view) {
        if (nativeOrJava.equals("java")) {
            nativeOrJava = "native";
            Toast.makeText(this, "Native Cam and methods", Toast.LENGTH_SHORT).show();
        } else {
            nativeOrJava = "java";
            Toast.makeText(this, "Java Cam and methods", Toast.LENGTH_SHORT).show();
        }
        recreate();
    }
}
