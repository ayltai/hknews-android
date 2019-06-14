package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.loader.HistoryItemLoader;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Single;

public final class HistoryListPresenter extends ListPresenter {
    @Nonnull
    @NonNull
    @Override
    protected ItemLoader createItemLoader() {
        return new HistoryItemLoader();
    }

    @Nonnull
    @NonNull
    @Override
    protected Single<Irrelevant> clear() {
        if (this.getView() == null || this.getModel() == null || this.getModel().isEmpty()) return Single.just(Irrelevant.INSTANCE);

        for (final Item item : this.getModel()) item.setLastAccessed(null);

        return ItemRepository.create(this.getView().getContext())
            .flatMap(repository -> repository.set(this.getModel()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
