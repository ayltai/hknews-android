package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

public class CozyListView extends ListView {
    public CozyListView(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final EmptyState emptyState) {
        super(context, emptyState);
    }

    @Override
    protected boolean isCompactStyle() {
        return false;
    }
}
