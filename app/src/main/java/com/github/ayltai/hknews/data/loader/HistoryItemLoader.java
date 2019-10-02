package com.github.ayltai.hknews.data.loader;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Single;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.HistoryItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

public final class HistoryItemLoader extends LocalItemLoader {
    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadLocally(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return HistoryItemRepository.create(context)
            .flatMap(repository -> repository.get(this.sourceNames, this.categoryNames, this.keywords))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
