#include "com_simjessimsol_simcv_detection_NativeDetection.h"
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/core/core.hpp>

#include <iostream>
#include <stdio.h>

using namespace cv;
using namespace std;

CascadeClassifier cascadeFile;
int scale = 2;
Mat grayFrame;
Size sizeOfMat;


JNIEXPORT void JNICALL Java_com_simjessimsol_simcv_detection_NativeDetection_sendCascadeFile
  (JNIEnv *jenv, jclass jnativeDetection, jstring jcascadeFilePath)
{
    const char *fixJavaFilePath = jenv->GetStringUTFChars(jcascadeFilePath, NULL);
    string getFilePame(fixJavaFilePath);
    cascadeFile.load(getFilePame);
    if (cascadeFile.empty())
    {
        cout << "ERROR: cascade file not found" << endl;
        return;
    }
}

JNIEXPORT void JNICALL Java_com_simjessimsol_simcv_detection_NativeDetection_sendScale
  (JNIEnv *jenv, jclass jnativeDetection, jint jscale)
{
    scale = jscale;
}

JNIEXPORT void JNICALL Java_com_simjessimsol_simcv_detection_NativeDetection_nativeDetectFace
  (JNIEnv *jenv, jclass jnativeDetection, jlong jframeAddress)
{
    vector<Rect> faces;
    Mat &colorFrame = *(Mat*) jframeAddress;

    cvtColor(colorFrame, grayFrame, CV_RGBA2GRAY);
    sizeOfMat = grayFrame.size();
    resize(grayFrame, grayFrame, Size(sizeOfMat.width / scale, sizeOfMat.height / scale));
    equalizeHist(grayFrame, grayFrame);

    cascadeFile.detectMultiScale(grayFrame, faces, 1.1, 3, 0|CV_HAAR_SCALE_IMAGE, Size(50, 50));

    for (size_t i = 0; i < faces.size(); i++)
    {
        Point center(faces[i].x * scale, faces[i].y * scale);
        Point stuff((faces[i].x + faces[i].width) * scale, (faces[i].y + faces[i].height) * scale);
        rectangle(colorFrame, center, stuff, Scalar(0, 0, 255), 3);
    }
}