package com.github.ayltai.hknews.widget;

import androidx.annotation.StringRes;

import com.github.ayltai.hknews.R;

public final class BookmarkEmptyState extends EmptyState {
    @StringRes
    @Override
    public int getTitle() {
        return R.string.empty_bookmark_title;
    }

    @StringRes
    @Override
    public int getDescription() {
        return R.string.empty_bookmark_description;
    }
}
