package com.github.ayltai.hknews.data.repository;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

public final class HistoryItemRepository extends ItemRepository {
    @Nonnull
    @NonNull
    public static Single<ItemRepository> create(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.defer(() -> Single.just(HistoryItemRepository.create(Components.getInstance()
            .getDataComponent(context)
            .realm()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER)));
    }

    @Nonnull
    @NonNull
    private static ItemRepository create(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        return new HistoryItemRepository(realm);
    }

    private HistoryItemRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    @Nonnull
    @NonNull
    @Override
    public Single<Irrelevant> deleteAll(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames) {
        return Single.defer(
            () -> {
                RealmQuery<Item> query = this.getRealm()
                    .where(Item.class)
                    .isNotNull(Item.FIELD_LAST_ACCESSED);

                if (!sourceNames.isEmpty()) query = query.in(Item.FIELD_SOURCE, sourceNames.toArray(new String[0]));
                if (!categoryNames.isEmpty()) query = query.in(Item.FIELD_CATEGORY, categoryNames.toArray(new String[0]));

                query.findAll().deleteAllFromRealm();

                return Single.just(Irrelevant.INSTANCE);
            })
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    @Override
    public Single<List<Item>> get(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        return Single.defer(() -> Single.just(this.get(this.getRealm().where(Item.class).isNotNull(Item.FIELD_LAST_ACCESSED), sourceNames, categoryNames, keywords)))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    @Override
    protected RealmQuery<Item> sort(@Nonnull @NonNull @lombok.NonNull final RealmQuery<Item> query) {
        return query.sort(new String[] { Item.FIELD_LAST_ACCESSED, Item.FIELD_PUBLISH_DATE }, new Sort[] { Sort.DESCENDING, Sort.DESCENDING });
    }
}
