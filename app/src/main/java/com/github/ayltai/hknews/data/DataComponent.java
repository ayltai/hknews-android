package com.github.ayltai.hknews.data;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import io.realm.Realm;

import dagger.Component;

@Component(modules = { DataModule.class })
public interface DataComponent {
    @Nonnull
    @NonNull
    Realm realm();
}
