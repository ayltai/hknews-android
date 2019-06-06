package com.github.ayltai.hknews.view;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.data.model.Item;

import java9.util.stream.Collectors;
import java9.util.stream.Stream;

public final class FeaturedListPresenter extends ModelPresenter<List<Item>, FeaturedListPresenter.View> {
    private boolean isInitialized;

    public interface View extends Presenter.View {
        void setItems(@Nonnull @NonNull List<Item> items);
    }

    @Override
    public void setModel(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        final List<Item> featuredItems = Stream.of(items.toArray(new Item[0]))
            .filter(item -> !item.getImages().isEmpty() || !item.getVideos().isEmpty())
            .filter(item -> System.currentTimeMillis() - item.getPublishDate().getTime() < Constants.FEATURED_NEWS_TIME)
            .collect(Collectors.toList());

        Collections.shuffle(featuredItems);

        super.setModel(featuredItems);
    }

    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null) this.getView().setItems(this.getModel());
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final FeaturedListPresenter.View view) {
        super.onViewAttached(view);

        if (!this.isInitialized) {
            this.isInitialized = true;

            this.bindModel();
        }
    }
}
