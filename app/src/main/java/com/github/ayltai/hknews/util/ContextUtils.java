package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ContextUtils {
    public int getColor(@Nonnull @NonNull @lombok.NonNull final Context context, @AttrRes final int colorAttribute) {
        final TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(colorAttribute, value, true) ? value.data : Color.TRANSPARENT;
    }
}
