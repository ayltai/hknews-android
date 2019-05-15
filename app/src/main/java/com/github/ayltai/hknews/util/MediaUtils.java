package com.github.ayltai.hknews.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.media.BaseImageLoaderCallback;
import com.github.piasy.biv.view.BigImageView;

import io.reactivex.disposables.Disposable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MediaUtils {
    private final int MAX_ZOOM = 8;

    private final List<Disposable> DISPOSABLES = new ArrayList<>();

    public void setAutoCenter(@Nonnull @NonNull @lombok.NonNull final BigImageView imageView) {
        imageView.setImageLoaderCallback(new BaseImageLoaderCallback() {
            @Override
            public void onSuccess(@Nonnull @NonNull @lombok.NonNull final File image) {
                MediaUtils.DISPOSABLES.add(Components.getInstance()
                    .getMediaComponent()
                    .faceCenterFinder()
                    .findFaceCenter(image)
                    .compose(RxUtils.applySingleBackgroundToMainSchedulers())
                    .subscribe(
                        center -> {
                            if (imageView.getSSIV() == null) return;

                            if (center.x >= 0 && center.y >= 0) imageView.getSSIV().setScaleAndCenter(imageView.getSSIV().getScale(), center);
                        },
                        RxUtils::handleError
                    ));
            }

            @Override
            public void onFinish() {
                MediaUtils.configure(imageView);
            }
        });
    }

    public void configure(@Nullable final BigImageView imageView) {
        if (imageView == null || imageView.getSSIV() == null) return;

        imageView.getSSIV().setMaxScale(MediaUtils.MAX_ZOOM);
        imageView.getSSIV().setZoomEnabled(false);
        imageView.getSSIV().setPanEnabled(false);
    }

    public void resetFaceDetector() {
        RxUtils.resetDisposables(MediaUtils.DISPOSABLES);
    }
}
