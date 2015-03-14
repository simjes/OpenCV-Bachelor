package com.simjessimsol.simcvgame.android;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.simjessimsol.simcvgame.MyGdxGame;
import com.simjessimsol.simcvgame.Player;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class AndroidLauncher extends AndroidApplication implements CvCameraViewListener2 {
    private final static String TAG = "android";

    private CameraBridgeViewBase cameraView;
    private Player player;
    private MyGdxGame myGdxGame;

    private Mat originalFrame;
    private Mat binaryFrame;
    private Scalar lowRed = new Scalar(163, 191, 211);
    private Scalar highRed = new Scalar(180, 255, 255);

    private int gdxWidth;
    private int gdxHeight;
    private float scaleX;
    private float scaleY;

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
        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);


        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        myGdxGame = new MyGdxGame();
        initialize(myGdxGame, config);
        cameraView = new JavaCameraView(this, 0);
        cameraView.setCvCameraViewListener(this);
        addContentView(cameraView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        player = myGdxGame.getPlayer();
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
        originalFrame = new Mat();
        binaryFrame = new Mat();
        player = myGdxGame.getPlayer();

        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        scaleX = ((float) gdxWidth) / width;
        scaleY = ((float) gdxHeight) / height;

        Log.i(TAG, "lol opencv width: " + width + ", height: " + height);
        Log.i(TAG, "lol width: " + Gdx.app.getGraphics().getWidth() + ", height: " + Gdx.app.getGraphics().getHeight());
        Log.i(TAG, "lol scaleX: " + scaleX + ", scaleY: " + scaleY);
        Log.i(TAG, "lol opencv with scale width: " + width * scaleX + ", height: " + height * scaleY);
    }

    @Override
    public void onCameraViewStopped() {
        originalFrame.release();
        binaryFrame.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        originalFrame = inputFrame.rgba();
        //Core.flip(originalFrame, originalFrame, 0);

        Imgproc.cvtColor(originalFrame, originalFrame, Imgproc.COLOR_RGB2HSV);
        Core.inRange(originalFrame, lowRed, highRed, binaryFrame);
        Point point = findCenterOfMass(binaryFrame);

        //TODO: check x + spritewidth and y + spriteheight
        if (point.x > 0 && point.x < gdxWidth && point.y > 0 && point.y < gdxHeight) {
            player.setX((int) (point.x * scaleX));
            player.setY(Math.abs((int) (gdxHeight - (point.y * scaleY))));
        }
        //Log.d(TAG, "faar frames: " + player);
        //Log.d(TAG, "org x: " + player.getX() + ", y: " + player.getY());
        /*if (player.getX() < 1000 && player.getY() < 200) {
            player.setX(player.getX() + 20);
            player.setY(player.getY() + 20);
            Log.d(TAG, "new x: " + player.getX() + ", y: " + player.getY());
        }*/
        return null;
    }

    private Point findCenterOfMass(Mat mat) {
        Moments matMoments = Imgproc.moments(mat, true);
        double matMoment10 = matMoments.get_m10();
        double matMoment01 = matMoments.get_m01();
        double matArea = matMoments.get_m00();

        int redPosX = (int) (matMoment10 / matArea);
        int redPosY = (int) (matMoment01 / matArea);

        return new Point(redPosX, redPosY);
    }
}
