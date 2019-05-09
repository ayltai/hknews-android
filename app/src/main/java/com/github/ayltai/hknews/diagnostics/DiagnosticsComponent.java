package com.github.ayltai.hknews.diagnostics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import dagger.Component;

@Component(modules = { DiagnosticsModule.class })
public interface DiagnosticsComponent {
    @Nonnull
    @NonNull
    PerformanceTrace performanceTrace();
}
