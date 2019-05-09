package com.github.ayltai.hknews.media;

import javax.inject.Singleton;

import com.github.ayltai.hknews.Components;
import com.github.piasy.biv.loader.ImageLoader;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import dagger.Module;
import dagger.Provides;

@Module
final class MediaModule {
    @Singleton
    @Provides
    static ImageLoader provideImageLoader() {
        return FrescoImageLoader.getInstance();
    }

    @Singleton
    @Provides
    static FaceCenterFinder provideFaceCenterFinder() {
        return new FirebaseFaceCenterFinder(new FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(Components.getInstance()
                .getConfigComponent()
                .remoteConfigurations()
                .getAccuracyForFaceDetection())
            .setMinFaceSize(Components.getInstance()
                .getConfigComponent()
                .remoteConfigurations()
                .getMinFaceSize())
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.NO_LANDMARKS)
            .setContourMode(FirebaseVisionFaceDetectorOptions.NO_CONTOURS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
            .build());
    }
}
