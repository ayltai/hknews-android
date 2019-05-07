package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;

import io.supercharge.shimmerlayout.ShimmerLayout;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AnimationUtils {
    public boolean areAnimatorsEnabled() {
        return !DevUtils.isRunningInstrumentedTest() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ValueAnimator.areAnimatorsEnabled());
    }

    public void startShimmerAnimation(@Nonnull @NonNull @lombok.NonNull final View view) {
        if (AnimationUtils.areAnimatorsEnabled()) {
            if (view instanceof ViewGroup) {
                final ViewGroup parent = (ViewGroup)view;

                for (int i = 0; i < parent.getChildCount(); i++) AnimationUtils.startShimmerAnimation(parent.getChildAt(i));

                if (view instanceof ShimmerLayout) ((ShimmerLayout)view).startShimmerAnimation();
            }
        }
    }

    public void stopShimmerAnimation(@Nonnull @NonNull @lombok.NonNull final View view) {
        if (AnimationUtils.areAnimatorsEnabled()) {
            if (view instanceof ViewGroup) {
                final ViewGroup parent = (ViewGroup)view;

                for (int i = 0; i < parent.getChildCount(); i++) AnimationUtils.stopShimmerAnimation(parent.getChildAt(i));

                if (view instanceof ShimmerLayout) ((ShimmerLayout)view).stopShimmerAnimation();
            }
        }
    }

    private int getAnimationDuration(@Nonnull @NonNull @lombok.NonNull final Context context, @IntegerRes final int durationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) return (int)(context.getResources().getInteger(durationId) * Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1f));

        return context.getResources().getInteger(durationId);
    }
}
