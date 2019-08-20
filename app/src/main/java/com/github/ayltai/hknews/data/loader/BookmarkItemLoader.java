package com.github.ayltai.hknews.data.loader;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.BookmarkItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

public final class BookmarkItemLoader extends ItemLoader {
    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadLocally(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return BookmarkItemRepository.create(context)
            .flatMap(repository -> repository.get(this.sourceNames, this.categoryNames, this.keywords))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.just(Collections.emptyList());
    }

    @Nonnull
    @NonNull
    @Override
    protected Consumer<? super List<Item>> onLoadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return items -> { };
    }

    @Override
    protected boolean isValid(@Nonnull @NonNull @lombok.NonNull final List<Item> item) {
        return true;
    }
}
