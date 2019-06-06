package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceUtils {
    public int getScreenWidth(@Nonnull @NonNull @lombok.NonNull final Context context) {
        final DisplayMetrics metrics = new DisplayMetrics();

        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }
}
