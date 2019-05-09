package com.github.ayltai.hknews.config;

public interface RemoteConfigurations {
    float getMinFaceSize();

    int getAccuracyForFaceDetection();

    int getMaxImageSizeForFaceDetection();

    boolean isPerformanceMonitoringEnabled();
}
