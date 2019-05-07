package com.github.ayltai.hknews.data.repository;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.DaggerDataComponent;
import com.github.ayltai.hknews.data.DataModule;
import com.github.ayltai.hknews.data.model.Source;
import com.github.ayltai.hknews.net.DaggerNetComponent;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

public final class SourceRepository extends Repository {
    @Nonnull
    @NonNull
    public static Single<SourceRepository> create(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.defer(() -> Single.just(SourceRepository.create(DaggerDataComponent.builder()
            .dataModule(new DataModule(context))
            .build()
            .realm()))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER)));
    }

    @Nonnull
    @NonNull
    public static SourceRepository create(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        return new SourceRepository(realm);
    }

    private SourceRepository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        super(realm);
    }

    @Nonnull
    @NonNull
    public Single<List<Source>> get() {
        return Single.defer(() -> {
            final RealmResults<Source> records = this.getRealm()
                .where(Source.class)
                .findAll();

            if (records.isEmpty()) return DaggerNetComponent.create()
                .apiService()
                .sources()
                .doOnSuccess(this::put);

            return Single.just(records);
        });
    }

    @Nonnull
    @NonNull
    private Single<Irrelevant> put(@Nonnull @NonNull @lombok.NonNull final Iterable<Source> sources) {
        return Single.defer(() -> {
            if (!this.getRealm().isInTransaction()) this.getRealm().beginTransaction();

            for (final Source source : sources) {
                final RealmResults<Source> records = this.getRealm()
                    .where(Source.class)
                    .equalTo(Source.FIELD_NAME, source.getName())
                    .findAll();

                if (records.isEmpty()) this.getRealm().insert(source);
            }

            if (this.getRealm().isInTransaction()) this.getRealm().commitTransaction();

            return Single.just(Irrelevant.INSTANCE);
        });
    }
}
