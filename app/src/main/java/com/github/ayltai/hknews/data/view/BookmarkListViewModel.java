package com.github.ayltai.hknews.data.view;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.loader.BookmarkItemLoader;
import com.github.ayltai.hknews.data.loader.ItemLoader;

public final class BookmarkListViewModel extends LocalListViewModel {
    public BookmarkListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    @Override
    protected ItemLoader getLoader() {
        final ItemLoader loader = new BookmarkItemLoader();

        loader.setSourceNames(Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getSourceNames());

        return loader;
    }
}
