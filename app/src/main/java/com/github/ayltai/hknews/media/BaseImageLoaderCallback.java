package com.github.ayltai.hknews.media;

import java.io.File;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.piasy.biv.loader.ImageLoader;

public class BaseImageLoaderCallback implements ImageLoader.Callback {
    @Override
    public void onCacheHit(final int imageType, @Nonnull @NonNull @lombok.NonNull final File image) {
    }

    @Override
    public void onCacheMiss(final int imageType, @Nonnull @NonNull @lombok.NonNull final File image) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onProgress(final int progress) {
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onSuccess(@Nonnull @NonNull @lombok.NonNull final File image) {
    }

    @Override
    public void onFail(@Nonnull @NonNull @lombok.NonNull final Exception error) {
    }
}
