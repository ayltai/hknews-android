package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceUtils {
    public int getScreenWidth(@Nonnull @NonNull @lombok.NonNull final Activity activity) {
        final DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }
}
