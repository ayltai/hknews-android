package com.github.ayltai.hknews.data.repository;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Single;
import io.realm.Realm;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.RxUtils;

public final class BookmarkItemRepository extends ItemRepository {
    @Nonnull
    @NonNull
    public static Single<ItemRepository> create(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.defer(() -> Single.just(BookmarkItemRepository.create(Components.getInstance()
            .getDataComponent(context)
            .realm()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER)));
    }

    @Nonnull
    @NonNull
    private static ItemRepository create(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        return new BookmarkItemRepository(realm);
    }

    private BookmarkItemRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    @Nonnull
    @NonNull
    @Override
    public Single<List<Item>> get(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        return Single.defer(() -> Single.just(this.get(this.getRealm().where(Item.class).equalTo(Item.FIELD_IS_BOOKMARKED, true), sourceNames, categoryNames, keywords)))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
