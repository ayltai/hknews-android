package com.github.ayltai.hknews.util;

import androidx.annotation.Nullable;

import com.github.piasy.biv.view.BigImageView;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MediaUtils {
    private static final int MAX_ZOOM = 8;

    public void configure(@Nullable final BigImageView imageView) {
        if (imageView == null || imageView.getSSIV() == null) return;

        imageView.getSSIV().setMaxScale(MediaUtils.MAX_ZOOM);
        imageView.getSSIV().setZoomEnabled(false);
        imageView.getSSIV().setPanEnabled(false);
    }
}
