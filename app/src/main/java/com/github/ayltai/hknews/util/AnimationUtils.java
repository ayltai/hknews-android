package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.supercharge.shimmerlayout.ShimmerLayout;

import flow.Direction;
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

    @Nullable
    public Animator createDefaultAnimator(@Nonnull @NonNull @lombok.NonNull final View view, @Nonnull @NonNull @lombok.NonNull final Direction direction, @Nullable final Runnable onStart, @Nullable final Runnable onEnd) {
        final Activity activity = ViewUtils.getActivity(view);
        if (activity == null) return null;

        final Animator animator = Direction.FORWARD == direction
            ? ObjectAnimator.ofFloat(view, "translationX", DeviceUtils.getScreenWidth(activity), 0).setDuration(AnimationUtils.getAnimationDuration(view.getContext(), android.R.integer.config_mediumAnimTime))
            : ObjectAnimator.ofFloat(view, "translationX", 0, DeviceUtils.getScreenWidth(activity)).setDuration(AnimationUtils.getAnimationDuration(view.getContext(), android.R.integer.config_mediumAnimTime));

        animator.setInterpolator(new AccelerateInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(@Nonnull @NonNull @lombok.NonNull final Animator animation) {
                if (onStart != null) onStart.run();
            }

            @Override
            public void onAnimationEnd(@Nonnull @NonNull @lombok.NonNull final Animator animation) {
                if (onEnd != null) onEnd.run();
            }
        });

        return animator;
    }

    private int getAnimationDuration(@Nonnull @NonNull @lombok.NonNull final Context context, @IntegerRes final int durationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) return (int)(context.getResources().getInteger(durationId) * Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1f));

        return context.getResources().getInteger(durationId);
    }
}
