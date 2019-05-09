package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

import dagger.Component;

@Singleton
@Component(modules = { AnalyticsModule.class })
public interface AnalyticsComponent {
    @Nonnull
    @NonNull
    EventLogger eventLogger();
}
