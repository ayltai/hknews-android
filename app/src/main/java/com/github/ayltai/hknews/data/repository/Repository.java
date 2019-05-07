package com.github.ayltai.hknews.data.repository;

import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class Repository implements LifecycleObserver {
    public static final Scheduler SCHEDULER = Schedulers.from(Executors.newSingleThreadExecutor());

    private final Realm realm;

    public Repository(@Nonnull @NonNull @lombok.NonNull final Realm realm) {
        this.realm = realm;
    }

    @Nonnull
    @NonNull
    public Realm getRealm() {
        return this.realm;
    }
}
