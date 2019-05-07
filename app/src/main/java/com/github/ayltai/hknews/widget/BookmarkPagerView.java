package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.view.PagerPresenter;

public final class BookmarkPagerView extends PagerView {
    public BookmarkPagerView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @Nonnull
    @NonNull
    @Override
    protected PagerAdapter createPagerAdapter() {
        return new BookmarkPagerAdapter((PagerPresenter)this.presenter);
    }
}
