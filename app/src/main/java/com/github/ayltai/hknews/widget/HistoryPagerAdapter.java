package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.view.HistoryListPresenter;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.PagerPresenter;

final class HistoryPagerAdapter extends PagerAdapter {
    public HistoryPagerAdapter(@Nonnull @NonNull @lombok.NonNull final PagerPresenter presenter) {
        super(presenter);
    }

    @Nonnull
    @NonNull
    @Override
    protected ListPresenter createListPresenter(@Nonnull @NonNull @lombok.NonNull final String category) {
        return new HistoryListPresenter(category);
    }
}
