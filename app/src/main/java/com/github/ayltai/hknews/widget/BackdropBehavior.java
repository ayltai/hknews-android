package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.util.AnimationUtils;
import com.google.android.material.appbar.AppBarLayout;

public final class BackdropBehavior extends CoordinatorLayout.Behavior<View> {
    //region Constants

    public static final int STATE_COLLAPSED = 1;
    public static final int STATE_EXPANDED  = 2;
    public static final int STATE_SETTLING  = 3;

    private static final String KEY_STATE = "STATE";

    //endregion

    @IntDef({
        BackdropBehavior.STATE_COLLAPSED,
        BackdropBehavior.STATE_EXPANDED,
        BackdropBehavior.STATE_SETTLING
    })
    public @interface State {}

    //region Variables

    private @BackdropBehavior.State int state       = BackdropBehavior.STATE_COLLAPSED;
    private @BackdropBehavior.State int targetState = BackdropBehavior.STATE_COLLAPSED;

    private boolean isInitialized;
    private int     height;

    //region Components

    private Toolbar      toolbar;
    private AppBarLayout backLayer;
    private View         frontLayer;

    //endregion

    //endregion

    //region Constructors

    public BackdropBehavior() {
    }

    public BackdropBehavior(@Nonnull @NonNull @lombok.NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    //endregion

    @BackdropBehavior.State
    public int getState() {
        return this.state;
    }

    public void expand() {
        if (this.targetState != BackdropBehavior.STATE_EXPANDED) {
            this.toggleTargetState();
            this.offsetFrontLayerTop();
        }
    }

    public void collapse() {
        if (this.targetState != BackdropBehavior.STATE_COLLAPSED) {
            this.toggleTargetState();
            this.offsetFrontLayerTop();
        }
    }

    @Override
    public boolean layoutDependsOn(@Nonnull @NonNull @lombok.NonNull final CoordinatorLayout parent, @Nonnull @NonNull @lombok.NonNull final View child, @Nonnull @NonNull @lombok.NonNull final View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@Nonnull @NonNull @lombok.NonNull final CoordinatorLayout parent, @Nonnull @NonNull @lombok.NonNull final View child, @Nonnull @NonNull @lombok.NonNull final View dependency) {
        this.frontLayer = child;
        this.backLayer  = (AppBarLayout)dependency;
        this.toolbar    = BackdropBehavior.findToolbar(this.backLayer);

        if (this.toolbar == null) return false;

        if (!this.isInitialized) {
            this.isInitialized = true;

            this.height = this.backLayer.getHeight();

            this.frontLayer.setY(this.getFrontLayerTop());

            final ViewGroup.LayoutParams frontLayerLayoutParams = this.frontLayer.getLayoutParams();
            frontLayerLayoutParams.height = this.getFrontLayerHeight(parent);
            this.frontLayer.setLayoutParams(frontLayerLayoutParams);

            this.setBackLayerHeight(this.toolbar.getHeight());

            this.toolbar.setNavigationIcon(this.getNavigationIcon());
            this.toolbar.setNavigationOnClickListener(view -> {
                this.toggleTargetState();

                this.setBackLayerHeight(this.targetState == BackdropBehavior.STATE_EXPANDED ? height : this.toolbar.getHeight());

                this.offsetFrontLayerTop();
            });
        }

        return true;
    }

    @Nonnull
    @NonNull
    @Override
    public Parcelable onSaveInstanceState(@Nonnull @NonNull @lombok.NonNull final CoordinatorLayout parent, @Nonnull @NonNull @lombok.NonNull final View child) {
        final Bundle bundle = new Bundle();
        bundle.putInt(BackdropBehavior.KEY_STATE, this.state);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(@Nonnull @NonNull @lombok.NonNull final CoordinatorLayout parent, @Nonnull @NonNull @lombok.NonNull final View child, @Nonnull @NonNull @lombok.NonNull final Parcelable state) {
        this.state = ((Bundle)state).getInt(BackdropBehavior.KEY_STATE, BackdropBehavior.STATE_COLLAPSED);
    }

    private void onBeforeStateChange() {
        this.state = BackdropBehavior.STATE_SETTLING;
    }

    private void onAfterStateChange() {
        this.state = this.targetState;

        this.setBackLayerHeight(this.state == BackdropBehavior.STATE_COLLAPSED ? this.toolbar.getHeight() : ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void toggleTargetState() {
        if (this.targetState == BackdropBehavior.STATE_COLLAPSED) {
            this.targetState = BackdropBehavior.STATE_EXPANDED;
        } else if (this.targetState == BackdropBehavior.STATE_EXPANDED) {
            this.targetState = BackdropBehavior.STATE_COLLAPSED;
        }

        this.toolbar.setNavigationIcon(this.getNavigationIcon());
    }

    private void offsetFrontLayerTop() {
        if (AnimationUtils.areAnimatorsEnabled()) {
            this.frontLayer.clearAnimation();

            this.frontLayer
                .animate()
                .y(this.getFrontLayerTop())
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(AnimationUtils.getAnimationDuration(this.frontLayer.getContext(), android.R.integer.config_mediumAnimTime))
                .withStartAction(this::onBeforeStateChange)
                .withEndAction(this::onAfterStateChange)
                .start();
        } else {
            this.onBeforeStateChange();
            this.frontLayer.setY(this.getFrontLayerTop());
            this.onAfterStateChange();
        }
    }

    private int getFrontLayerTop() {
        return this.targetState == BackdropBehavior.STATE_COLLAPSED ? this.toolbar.getHeight() : this.backLayer.getLayoutParams().height;
    }

    private int getFrontLayerHeight(@Nonnull @NonNull @lombok.NonNull final CoordinatorLayout parent) {
        return parent.getHeight() - this.toolbar.getHeight();
    }

    private void setBackLayerHeight(final int height) {
        final ViewGroup.LayoutParams params = this.backLayer.getLayoutParams();
        params.height = height;
        this.backLayer.setLayoutParams(params);
    }

    private int getNavigationIcon() {
        return this.targetState == BackdropBehavior.STATE_COLLAPSED ? R.drawable.ic_menu_white_24dp : R.drawable.ic_close_white_24dp;
    }

    @Nullable
    private static Toolbar findToolbar(@Nonnull @NonNull @lombok.NonNull final ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);

            if (child instanceof Toolbar) return (Toolbar)child;
        }

        return null;
    }
}
