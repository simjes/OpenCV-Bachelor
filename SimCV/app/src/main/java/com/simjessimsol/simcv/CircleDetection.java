package com.simjessimsol.simcv;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CircleDetection extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private final static String TAG = "com.simjessimsol.simcv";

    private final static String STATE_CAMERA_INDEX = "cameraIndex";

    private CameraBridgeViewBase cameraView;
    private int cameraIndex;
    private boolean isCameraFrontFacing;
    private int numberOfCameras;
    private static Mat grayscaleImage;
    private static Mat circles;

    private Mat inputFrame;
    private Mat detectedImage;
    private int scale = 3;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(TAG, "OpenCV loaded successfully");
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

        setContentView(R.layout.activity_circledetection);

        if (savedInstanceState != null) {
            cameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
        } else {
            cameraIndex = 0;
        }

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

        cameraView = (CameraBridgeViewBase) findViewById(R.id.CircleDetectionCameraView);
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
        inputFrame = new Mat();
        detectedImage = new Mat();
        grayscaleImage = new Mat();
        circles = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        inputFrame.release();
        detectedImage.release();
        grayscaleImage.release();
        circles.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inFrame) {
        inputFrame = inFrame.rgba();
        if (isCameraFrontFacing) {
            Core.flip(inputFrame, inputFrame, 1);
        }
        detectedImage = findCircle(inputFrame);
        return detectedImage;
    }

    private Mat findCircle(Mat originalImage) {
        Imgproc.cvtColor(originalImage, grayscaleImage, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.resize(grayscaleImage, grayscaleImage, new Size(originalImage.size().width / scale, originalImage.size().height / scale));
        Imgproc.GaussianBlur(grayscaleImage, grayscaleImage, new Size(9, 9), 2, 2);
        Imgproc.Canny(grayscaleImage, grayscaleImage, 10, 30);

        Imgproc.HoughCircles(grayscaleImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1, grayscaleImage.rows() / 8, 200, 100, 0, 0);

        //Log.w("circles", circles.cols()+"");
        if (circles.cols() > 0) {
            for (int x = 0; x < circles.cols(); x++) {
                double vectorCircle[] = circles.get(0, x);

                if (vectorCircle == null)
                    break;

                Point pt = new Point(Math.round(vectorCircle[0]) * scale, Math.round(vectorCircle[1]) * scale);
                int radius = (int) Math.round(vectorCircle[2]) * scale;

                Core.circle(originalImage, pt, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(originalImage, pt, radius, new Scalar(0, 0, 255), 3, 8, 0);
            }
        }

        return originalImage;
    }

    public void changeCameraClick(View view) {
        cameraView.disableView();
        if (cameraIndex == 0) {
            cameraIndex = 1;
            cameraView.setCameraIndex(cameraIndex);
        } else {
            cameraIndex = 0;
            cameraView.setCameraIndex(cameraIndex);
        }
        cameraView.enableView();
    }

}
