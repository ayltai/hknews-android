package com.github.ayltai.hknews.data.loader;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

import lombok.Getter;
import lombok.Setter;

public abstract class Loader<T> {
    @Getter
    @Setter
    private boolean isForcedRefresh;

    @Nonnull
    @NonNull
    public Single<T> load(@Nonnull @NonNull @lombok.NonNull final Context context) {
        if (this.isForcedRefresh) return this.loadRemotely(context)
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .doOnSuccess(this.onLoadRemotely(context));

        return this.loadLocally(context)
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .concatWith(this.loadRemotely(context)
                .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
                .doOnSuccess(this.onLoadRemotely(context)))
            .compose(RxUtils.applyFlowableSchedulers(Repository.SCHEDULER))
            .filter(this::isValid)
            .firstOrError()
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    protected abstract Single<T> loadLocally(@Nonnull @NonNull @lombok.NonNull Context context);

    @Nonnull
    @NonNull
    protected abstract Single<T> loadRemotely(@Nonnull @NonNull @lombok.NonNull Context context);

    @Nonnull
    @NonNull
    protected abstract Consumer<? super T> onLoadRemotely(@Nonnull @NonNull @lombok.NonNull Context context);

    protected abstract boolean isValid(@Nonnull @NonNull @lombok.NonNull T items);
}
