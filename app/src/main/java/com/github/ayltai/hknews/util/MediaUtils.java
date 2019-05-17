package com.github.ayltai.hknews.util;

import androidx.annotation.Nullable;

import com.github.ayltai.hknews.Constants;
import com.github.piasy.biv.view.BigImageView;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MediaUtils {
    public void configure(@Nullable final BigImageView imageView) {
        if (imageView == null || imageView.getSSIV() == null) return;

        imageView.getSSIV().setMaxScale(Constants.MAX_ZOOM);
        imageView.getSSIV().setZoomEnabled(false);
        imageView.getSSIV().setPanEnabled(false);
    }
}
