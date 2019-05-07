package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

public class CompactListView extends ListView {
    public CompactListView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @Override
    protected boolean isCompactStyle() {
        return true;
    }
}
