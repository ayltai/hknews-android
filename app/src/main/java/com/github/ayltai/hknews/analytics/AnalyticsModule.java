package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
final class AnalyticsModule {
    @Singleton
    @Nonnull
    @NonNull
    @Provides
    static EventLogger provideEventLogger() {
        return new FirebaseEventLogger();
    }
}
