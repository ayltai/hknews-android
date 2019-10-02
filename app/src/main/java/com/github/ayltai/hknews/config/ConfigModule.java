package com.github.ayltai.hknews.config;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import android.content.Context;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfigModule {
    private ConfigModule() {
    }

    public static void init(@Nonnull @NonNull @lombok.NonNull final Context context) {
        FirebaseRemoteConfigurations.init();
        PreferenceUserConfigurations.init(context);
    }

    @Singleton
    @Nonnull
    @NonNull
    @Provides
    static UserConfigurations provideUserConfigurations() {
        return PreferenceUserConfigurations.getInstance();
    }

    @Singleton
    @Nonnull
    @NonNull
    @Provides
    static RemoteConfigurations provideRemoteConfigurations() {
        return new FirebaseRemoteConfigurations();
    }
}
