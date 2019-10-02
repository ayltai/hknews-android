package com.github.ayltai.hknews.data.repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Single;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

public class ItemRepository extends Repository {
    @Nonnull
    @NonNull
    public static Single<ItemRepository> create(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.defer(() -> Single.just(ItemRepository.create(Components.getInstance()
            .getDataComponent(context)
            .realm()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER)));
    }

    @Nonnull
    @NonNull
    private static ItemRepository create(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        return new ItemRepository(realm);
    }

    protected ItemRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    @Nonnull
    @NonNull
    public Single<Irrelevant> deleteAll(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> set(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        return Single.defer(
            () -> {
                if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();
                this.getRealm().copyToRealm(items);
                if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

                return Single.just(items);
            })
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<Item> setLastAccessed(@Nonnull @NonNull @lombok.NonNull final String itemUrl, @Nonnull @NonNull @lombok.NonNull final Date date) {
        return Single.defer(
            () -> {
                Item item = this.getRealm().where(Item.class).equalTo(Item.FIELD_URL, itemUrl).findFirst();

                if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();

                item.setLastAccessed(date);
                item = this.getRealm().copyToRealmOrUpdate(item);

                if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

                return Single.just(this.getRealm().copyFromRealm(item));
            })
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<Item> setIsBookmarked(@Nonnull @NonNull @lombok.NonNull final String itemUrl, final boolean isBookmarked) {
        return Single.defer(
            () -> {
                Item item = this.getRealm().where(Item.class).equalTo(Item.FIELD_URL, itemUrl).findFirst();

                if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();

                item.setIsBookmarked(isBookmarked);
                item = this.getRealm().copyToRealmOrUpdate(item);

                if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

                return Single.just(this.getRealm().copyFromRealm(item));
            })
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<Item> get(@Nonnull @NonNull @lombok.NonNull final String itemUrl) {
        return Single.defer(() -> Single.just(this.getRealm().copyFromRealm(this.getRealm().where(Item.class).equalTo(Item.FIELD_URL, itemUrl).findFirst())))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> get(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        return Single.defer(() -> Single.just(this.get(this.getRealm().where(Item.class), sourceNames, categoryNames, keywords)))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    protected List<Item> get(@Nonnull @NonNull @lombok.NonNull RealmQuery<Item> query, @Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final List<String> categoryNames, @Nullable final String keywords) {
        if (!sourceNames.isEmpty()) query = query.in(Item.FIELD_SOURCE, sourceNames.toArray(new String[0]));
        if (!categoryNames.isEmpty()) query = query.in(Item.FIELD_CATEGORY, categoryNames.toArray(new String[0]));

        if (keywords != null) query = query.beginGroup()
            .contains(Item.FIELD_TITLE, keywords, Case.INSENSITIVE)
            .or()
            .contains(Item.FIELD_DESCRIPTION, keywords, Case.INSENSITIVE)
            .endGroup();

        return this.getRealm().copyFromRealm(this.sort(query).findAll());
    }

    @Nonnull
    @NonNull
    protected RealmQuery<Item> sort(@Nonnull @NonNull @lombok.NonNull final RealmQuery<Item> query) {
        return query.sort(Item.FIELD_PUBLISH_DATE, Sort.DESCENDING);
    }
}
