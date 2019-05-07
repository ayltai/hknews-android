package com.github.ayltai.hknews.net;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = { NetModule.class })
public interface NetComponent {
    @Nonnull
    @NonNull
    OkHttpClient okHttpClient();

    @Nonnull
    @NonNull
    ApiService apiService();
}
