package com.github.ayltai.hknews.widget;

import androidx.annotation.StringRes;

import com.github.ayltai.hknews.R;

public final class HistoryEmptyState extends EmptyState {
    @StringRes
    @Override
    public int getTitle() {
        return R.string.empty_history_title;
    }

    @StringRes
    @Override
    public int getDescription() {
        return R.string.empty_history_description;
    }
}
