package com.github.ayltai.hknews.diagnostics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

public interface PerformanceTrace {
    void start(@Nonnull @NonNull String name);

    void stop();
}
