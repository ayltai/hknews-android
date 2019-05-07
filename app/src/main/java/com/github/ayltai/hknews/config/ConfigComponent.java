package com.github.ayltai.hknews.config;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

import dagger.Component;

@Singleton
@Component(modules = { ConfigModule.class })
public interface ConfigComponent {
    @Nonnull
    @NonNull
    UserConfigurations userConfigurations();
}
