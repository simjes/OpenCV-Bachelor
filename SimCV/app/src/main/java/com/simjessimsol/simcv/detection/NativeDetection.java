package com.simjessimsol.simcv.detection;

public class NativeDetection {

    public static native void sendCascadeFile(String cascadeFilePath);

    public static native void sendScale(int scale);

    public static native void nativeDetectFace(long frameAddress);
}
