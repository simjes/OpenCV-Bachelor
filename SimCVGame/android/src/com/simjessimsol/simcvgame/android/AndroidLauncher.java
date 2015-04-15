package com.simjessimsol.simcvgame.android;

import android.os.Bundle;
import android.view.View;
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        myGdxGame = new MyGdxGame();
        initialize(myGdxGame, config);
        cameraView = new JavaCameraView(this, 1);
        cameraView.setCvCameraViewListener(this);
        addContentView(cameraView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, loaderCallback);
        } else {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        originalFrame = new Mat();
        binaryFrame = new Mat();

        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        scaleX = ((float) gdxWidth) / width;
        scaleY = ((float) gdxHeight) / height;

        player = myGdxGame.getPlayer();
    }

    @Override
    public void onCameraViewStopped() {
        originalFrame.release();
        binaryFrame.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        if (myGdxGame.getCurrentState() == MyGdxGame.GameState.RUN) {
            originalFrame = inputFrame.rgba();
            Core.flip(originalFrame, originalFrame, 1);

            Imgproc.cvtColor(originalFrame, originalFrame, Imgproc.COLOR_RGB2HSV);
            Core.inRange(originalFrame, lowRed, highRed, binaryFrame);
            Point point = findCenterOfMass(binaryFrame);

            if (point.x - player.getWidth() / 2 > 0 && point.x + player.getWidth() / 2 < gdxWidth) {
                player.setX((int) (point.x * scaleX));
            }
        }
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
