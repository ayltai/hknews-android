package com.github.ayltai.hknews.data.view;

import java.util.Date;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Single;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

public final class DetailsViewModel extends AndroidViewModel {
    public DetailsViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    public Single<Item> get(@Nonnull @NonNull @lombok.NonNull final String itemUrl) {
        return ItemRepository.create(this.getApplication())
            .flatMap(repository -> repository.get(itemUrl))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<Item> setLastAccess(@Nonnull @NonNull @lombok.NonNull final String itemUrl, @Nonnull @NonNull @lombok.NonNull final Date date) {
        return ItemRepository.create(this.getApplication())
            .flatMap(repository -> repository.setLastAccessed(itemUrl, date))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<Item> setIsBookmarked(@Nonnull @NonNull @lombok.NonNull final String itemUrl, final boolean isBookmarked) {
        return ItemRepository.create(this.getApplication())
            .flatMap(repository -> repository.setIsBookmarked(itemUrl, isBookmarked))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
