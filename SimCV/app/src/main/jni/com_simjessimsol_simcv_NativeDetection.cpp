#include "com_simjessimsol_simcv_NativeDetection.h"
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/core/core.hpp>

#include <iostream>
#include <stdio.h>

using namespace cv;
using namespace std;

CascadeClassifier cascadeFile;
int SCALE = 2;
Mat grayFrame;
Size sizeOfMat;


JNIEXPORT void JNICALL Java_com_simjessimsol_simcv_NativeDetection_sendCascadeFile
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

JNIEXPORT void JNICALL Java_com_simjessimsol_simcv_NativeDetection_nativeDetectFace
  (JNIEnv *jenv, jclass jnativeDetection, jlong jframeAddress)
{
    vector<Rect> faces;
    Mat &colorFrame = *(Mat*) jframeAddress;

    cvtColor(colorFrame, grayFrame, CV_RGBA2GRAY);
    equalizeHist(grayFrame, grayFrame);

    sizeOfMat = grayFrame.size();
    resize(grayFrame, grayFrame, Size(sizeOfMat.width / SCALE, sizeOfMat.height / SCALE));

    cascadeFile.detectMultiScale(grayFrame, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(50, 50));

    for (size_t i = 0; i < faces.size(); i++)
    {
        Point center(faces[i].x * SCALE, faces[i].y * SCALE);
        Point stuff((faces[i].x + faces[i].width) * SCALE, (faces[i].y + faces[i].height) * SCALE);
        rectangle(colorFrame, center, stuff, Scalar(0, 0, 255), 3);
    }
}