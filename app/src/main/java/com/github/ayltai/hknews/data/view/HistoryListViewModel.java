package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;

import io.reactivex.Single;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.loader.HistoryItemLoader;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.repository.HistoryItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

public final class HistoryListViewModel extends LocalListViewModel {
    public HistoryListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    @Override
    public Single<Irrelevant> clear(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final String category) {
        return HistoryItemRepository.create(this.getApplication())
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .flatMap(repository -> repository.deleteAll(sourceNames, Collections.singletonList(category)))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    @Override
    protected ItemLoader getLoader() {
        final ItemLoader loader = new HistoryItemLoader();

        loader.setSourceNames(Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getSourceNames());

        return loader;
    }
}
