package com.simjessimsol.simcv.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.simjessimsol.simcv.Performance;
import com.simjessimsol.simcv.R;

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

public class FaceDetection extends Activity implements CvCameraViewListener2 {

    private final static String TAG = "detection";
    private final static String STATE_CAMERA_INDEX = "cameraIndex";
    private final static String STATE_NATIVE_OR_JAVA = "nativeOrJava";

    private Performance performanceCounter;

    private CameraBridgeViewBase cameraView;
    private int cameraIndex;
    private boolean isCameraFrontFacing;
    private String nativeOrJava;

    private Mat inputFrame;
    private Mat grayscaleImage;
    private CascadeClassifier detector;
    private int scale;

    private boolean drop = false;
    private Rect[] lastRect;

    private boolean isAlternativeCamera;

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
                        File cascadeFile = new File(cascadeDir, "lbpcascade_face.xml");
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
                        NativeDetection.sendScale(scale);

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_facedetection);
        isAlternativeCamera = getIntent().getBooleanExtra("isAlternativeCamera", false);
        scale = getIntent().getIntExtra("setScale", 1);
        Log.d(TAG, "FaceScale: " + scale);

        if (savedInstanceState != null) {
            cameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
            nativeOrJava = savedInstanceState.getString(STATE_NATIVE_OR_JAVA);
        } else {
            cameraIndex = 0;
            nativeOrJava = "java";
        }

        int numberOfCameras;
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

        /*if (nativeOrJava.equals("java")) {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCVCamView);
        } else {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.NativeOpenCVCamView);
        }*/
        if (isAlternativeCamera) {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.NativeOpenCVCamView);
        } else {
            cameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCVCamView);
        }
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCameraIndex(cameraIndex);
        cameraView.setCvCameraViewListener(this);

        performanceCounter = new Performance();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CAMERA_INDEX, cameraIndex);
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
        grayscaleImage = new Mat();

        performanceCounter.startFPSCounter();
    }

    @Override
    public void onCameraViewStopped() {
        inputFrame.release();
        grayscaleImage.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inFrame) {
        performanceCounter.start();
        performanceCounter.addFrame();
        inputFrame = inFrame.rgba();
        if (isCameraFrontFacing) {
            Core.flip(inputFrame, inputFrame, 1);
        }
        //TODO: skip detect on frames for better fps?
        if (nativeOrJava.equals("java")) {
            if (!drop) {
                drop = true;
                findFaces();
                performanceCounter.stop();
                return inputFrame;
            } else {
                for (Rect r : lastRect) {
                    Core.rectangle(inputFrame, new Point(r.x * scale, r.y * scale), new Point((r.x + r.width) * scale, (r.y + r.height) * scale), new Scalar(0, 0, 255), 3);
                }
                drop = false;
                performanceCounter.stop();
                return inputFrame;
            }
        } else {
            NativeDetection.nativeDetectFace(inputFrame.getNativeObjAddr());
            performanceCounter.stop();
            return inputFrame;
        }
    }


    public void changeCameraClick(View view) {
        cameraView.disableView();
        cameraIndex ^= 1;
        cameraView.setCameraIndex(cameraIndex);
        cameraView.enableView();
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

    private void findFaces() {
        Imgproc.cvtColor(inputFrame, grayscaleImage, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.resize(grayscaleImage, grayscaleImage, new Size(inputFrame.size().width / scale, inputFrame.size().height / scale));
        Imgproc.equalizeHist(grayscaleImage, grayscaleImage);

        MatOfRect detectedFaces = new MatOfRect();
        detector.detectMultiScale(grayscaleImage, detectedFaces, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, new Size(50, 50), new Size());

        lastRect = detectedFaces.toArray();
        for (Rect r : detectedFaces.toArray()) {
            Core.rectangle(inputFrame, new Point(r.x * scale, r.y * scale), new Point((r.x + r.width) * scale, (r.y + r.height) * scale), new Scalar(0, 0, 255), 3);
        }
    }
}
