package com.github.ayltai.hknews.media;

import javax.inject.Singleton;

import com.github.piasy.biv.loader.ImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
final class MediaModule {
    private MediaModule() {
    }

    @Singleton
    @Provides
    static ImageLoader provideImageLoader() {
        return FrescoImageLoader.getInstance();
    }

    @Singleton
    @Provides
    static CenterFinder provideCenterFinder() {
        return new TensorFlowCenterFinder();
    }
}
