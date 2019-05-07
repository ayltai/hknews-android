package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.loader.BookmarkItemLoader;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Single;

public final class BookmarkListPresenter extends ListPresenter {
    public BookmarkListPresenter(@Nonnull @NonNull @lombok.NonNull final String categoryName) {
        super(categoryName);
    }

    @Nonnull
    @NonNull
    @Override
    protected ItemLoader createItemLoader() {
        return new BookmarkItemLoader();
    }

    @Nonnull
    @NonNull
    @Override
    protected Single<Irrelevant> clear() {
        if (this.getView() == null || this.getModel() == null || this.getModel().isEmpty()) return Single.just(Irrelevant.INSTANCE);

        for (final Item item : this.getModel()) item.setBookmarked(false);

        return ItemRepository.create(this.getView().getContext())
            .flatMap(repository -> repository.set(this.getModel()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
