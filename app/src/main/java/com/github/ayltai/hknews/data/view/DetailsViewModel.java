package com.github.ayltai.hknews.data.view;

import java.util.Date;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Single;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;

public final class DetailsViewModel extends AndroidViewModel {
    public DetailsViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    public Single<Item> get(@Nonnull @NonNull @lombok.NonNull final String itemUrl) {
        return ItemRepository.create(Components.getInstance()
            .getDataComponent(this.getApplication())
            .realm())
            .get(itemUrl);
    }

    @Nonnull
    @NonNull
    public Single<Item> setLastAccess(@Nonnull @NonNull @lombok.NonNull final String itemUrl, @Nonnull @NonNull @lombok.NonNull final Date date) {
        final ItemRepository repository = ItemRepository.create(Components.getInstance()
            .getDataComponent(this.getApplication())
            .realm());

        return repository
            .get(itemUrl)
            .map(item -> {
                item.setLastAccessed(date);

                return item;
            })
            .flatMap(repository::set);
    }

    @Nonnull
    @NonNull
    public Single<Item> setIsBookmarked(@Nonnull @NonNull @lombok.NonNull final String itemUrl, final boolean isBookmarked) {
        final ItemRepository repository = ItemRepository.create(Components.getInstance()
            .getDataComponent(this.getApplication())
            .realm());

        return repository
            .get(itemUrl)
            .map(item -> {
                item.setBookmarked(isBookmarked);

                return item;
            })
            .flatMap(repository::set);
    }
}
