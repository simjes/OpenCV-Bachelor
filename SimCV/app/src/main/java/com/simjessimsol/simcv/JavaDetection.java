package com.simjessimsol.simcv;

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

public class JavaDetection {
    private static Mat grayscaleImage = new Mat();
    private static Mat circles = new Mat();

    public static Mat findFaces(Mat originalImage, int SCALE, CascadeClassifier detector) {

        Imgproc.cvtColor(originalImage, grayscaleImage, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.resize(grayscaleImage, grayscaleImage, new Size(originalImage.size().width / SCALE, originalImage.size().height / SCALE));
        Imgproc.equalizeHist(grayscaleImage, grayscaleImage);

        MatOfRect detectedFaces = new MatOfRect();
        detector.detectMultiScale(grayscaleImage, detectedFaces);//, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(50, 50), new Size());

        for (Rect r : detectedFaces.toArray()) {
            Core.rectangle(originalImage, new Point(r.x * SCALE, r.y * SCALE), new Point((r.x + r.width) * SCALE, (r.y + r.height) * SCALE), new Scalar(0, 0, 255), 3);
        }

        return originalImage;
    }

    /**
     * http://docs.opencv.org/doc/tutorials/imgproc/imgtrans/hough_circle/hough_circle.html
     * http://stackoverflow.com/questions/9445102/detecting-hough-circles-android
     *
     * @param originalImage
     * @return
     */
    public static Mat findCircle(Mat originalImage, int SCALE) {
        Imgproc.cvtColor(originalImage, grayscaleImage, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.resize(grayscaleImage, grayscaleImage, new Size(originalImage.size().width / SCALE, originalImage.size().height / SCALE));
        Imgproc.GaussianBlur(grayscaleImage, grayscaleImage, new Size(9, 9), 2, 2);
        Imgproc.Canny(grayscaleImage, grayscaleImage, 10, 30);

        Imgproc.HoughCircles(grayscaleImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1, grayscaleImage.rows() / 8, 200, 100, 0, 0);

        //Log.w("circles", circles.cols()+"");
        if (circles.cols() > 0) {
            for (int x = 0; x < circles.cols(); x++) {
                double vectorCircle[] = circles.get(0, x);

                if (vectorCircle == null)
                    break;

                Point pt = new Point(Math.round(vectorCircle[0]) * SCALE, Math.round(vectorCircle[1]) * SCALE);
                int radius = (int) Math.round(vectorCircle[2]) * SCALE;

                Core.circle(originalImage, pt, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(originalImage, pt, radius, new Scalar(0, 0, 255), 3, 8, 0);
            }
        }

        return originalImage;
    }

}
