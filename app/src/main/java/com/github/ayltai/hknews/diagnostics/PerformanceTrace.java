package com.github.ayltai.hknews.diagnostics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

public abstract class PerformanceTrace {
    public abstract void start(@Nonnull @NonNull @lombok.NonNull String name);

    public abstract void stop();
}
