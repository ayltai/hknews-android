package com.github.ayltai.hknews.diagnostics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
final class DiagnosticsModule {
    private DiagnosticsModule() {
    }

    @Nonnull
    @NonNull
    @Provides
    static PerformanceTrace providePerformanceTrace() {
        return new FirebasePerformanceTrace();
    }
}
