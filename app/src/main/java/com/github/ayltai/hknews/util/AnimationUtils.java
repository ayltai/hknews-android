package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AnimationUtils {
    public boolean areAnimatorsEnabled() {
        return !DevUtils.isRunningInstrumentedTest() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ValueAnimator.areAnimatorsEnabled());
    }

    public int getAnimationDuration(@Nonnull @NonNull @lombok.NonNull final Context context, @IntegerRes final int durationId) {
        return (int)(context.getResources().getInteger(durationId) * Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1f));
    }
}
