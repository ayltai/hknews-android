package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.util.Screen;

public abstract class ScreenView extends BaseView implements Screen {
    protected ScreenView(@Nonnull @NonNull final @lombok.NonNull Context context) {
        super(context);
    }
}
