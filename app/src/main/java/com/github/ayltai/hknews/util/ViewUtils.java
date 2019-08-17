package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ViewUtils {
    //region Constants

    public static final int LAYOUT_DEFAULT    = 0;
    public static final int LAYOUT_SCREEN     = 1;
    public static final int LAYOUT_MAX_WIDTH  = 2;
    public static final int LAYOUT_MAX_HEIGHT = 3;

    //endregion

    @IntDef({ ViewUtils.LAYOUT_DEFAULT, ViewUtils.LAYOUT_SCREEN, ViewUtils.LAYOUT_MAX_WIDTH, ViewUtils.LAYOUT_MAX_HEIGHT })
    public @interface Layout {}

    @Nullable
    public Activity getActivity(@Nonnull @NonNull @lombok.NonNull final View view) {
        return ViewUtils.getActivity(view.getContext());
    }

    @Nullable
    public Activity getActivity(@Nullable final Context context) {
        if (context instanceof ContextWrapper) {
            if (context instanceof Activity) return (Activity)context;

            return ViewUtils.getActivity(((ContextWrapper)context).getBaseContext());
        }

        return null;
    }

    public void updateLayout(@Nonnull @NonNull @lombok.NonNull final View view, @ViewUtils.Layout final int layout) {
        view.setLayoutParams(new FrameLayout.LayoutParams(layout == ViewUtils.LAYOUT_SCREEN || layout == ViewUtils.LAYOUT_MAX_WIDTH ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, layout == ViewUtils.LAYOUT_SCREEN || layout == ViewUtils.LAYOUT_MAX_HEIGHT ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
