package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.view.BookmarkListPresenter;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.PagerPresenter;

final class BookmarkPagerAdapter extends PagerAdapter {
    public BookmarkPagerAdapter(@Nonnull @NonNull @lombok.NonNull final PagerPresenter presenter) {
        super(presenter);
    }

    @Nonnull
    @NonNull
    @Override
    protected ListPresenter createListPresenter(@Nonnull @NonNull @lombok.NonNull final String category) {
        return new BookmarkListPresenter(category);
    }
}
