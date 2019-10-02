package com.github.ayltai.hknews.data.repository;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmQuery;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

public abstract class DeletableItemRepository extends ItemRepository {
    protected DeletableItemRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    protected Single<Irrelevant> deleteAll(@Nonnull @NonNull @lombok.NonNull final RealmQuery<Item> query, @Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames) {
        return Single.defer(
            () -> {
                RealmQuery<Item> q = query;

                if (!sourceNames.isEmpty()) q = q.in(Item.FIELD_SOURCE, sourceNames.toArray(new String[0]));
                if (!categoryNames.isEmpty()) q = q.in(Item.FIELD_CATEGORY, categoryNames.toArray(new String[0]));

                q.findAll().deleteAllFromRealm();

                return Single.just(Irrelevant.INSTANCE);
            })
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
