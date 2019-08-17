package com.github.ayltai.hknews.data.repository;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

public class ItemRepository extends Repository {
    @Nonnull
    @NonNull
    public static Single<ItemRepository> create(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.defer(() -> Single.just(ItemRepository.create(Components.getInstance()
            .getDataComponent(context)
            .realm()))
            .compose(RxUtils.applySingleSchedulers(ItemRepository.SCHEDULER)));
    }

    @Nonnull
    @NonNull
    public static ItemRepository create(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        return new ItemRepository(realm);
    }

    protected ItemRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    @Nonnull
    @NonNull
    public Single<Item> set(@Nonnull @NonNull @lombok.NonNull final Item item) {
        return Single.defer(() -> {
            if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();
            this.getRealm().copyToRealmOrUpdate(item);
            if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

            return Single.just(item);
        });
    }

    @Nonnull
    @NonNull
    public Single<Irrelevant> set(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        return Single.defer(() -> {
            if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();
            this.getRealm().copyToRealmOrUpdate(items);
            if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

            return Single.just(Irrelevant.INSTANCE);
        });
    }

    @Nonnull
    @NonNull
    public Single<Item> get(@Nonnull @NonNull @lombok.NonNull final String itemUrl) {
        return Single.defer(() -> Single.just(this.getRealm().copyFromRealm(this.getRealm().where(Item.class).equalTo(Item.FIELD_URL, itemUrl).findFirst())));
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> get(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        return Single.defer(() -> Single.just(this.get(this.getRealm().where(Item.class), sourceNames, categoryNames, keywords)));
    }

    protected List<Item> get(@Nonnull @NonNull @lombok.NonNull RealmQuery<Item> query, @Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        if (!sourceNames.isEmpty()) query = query.in(Item.FIELD_SOURCE, sourceNames.toArray(new String[0]));
        if (!categoryNames.isEmpty()) query = query.in(Item.FIELD_CATEGORY, categoryNames.toArray(new String[0]));

        if (keywords != null) query = query.beginGroup()
            .contains(Item.FIELD_TITLE, keywords)
            .or()
            .contains(Item.FIELD_DESCRIPTION, keywords)
            .endGroup();

        return this.getRealm().copyFromRealm(this.sort(query).findAll());
    }

    @Nonnull
    @NonNull
    protected RealmQuery<Item> sort(@Nonnull @NonNull @lombok.NonNull final RealmQuery<Item> query) {
        return query.sort(Item.FIELD_PUBLISH_DATE, Sort.DESCENDING);
    }
}
