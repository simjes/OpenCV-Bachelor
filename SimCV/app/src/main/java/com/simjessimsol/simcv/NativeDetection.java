package com.simjessimsol.simcv;

public class NativeDetection {

    public static native void sendCascadeFile(String cascadeFilePath);

    public static native void nativeDetectFace(long frameAddress);

}
