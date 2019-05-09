package com.github.ayltai.hknews.diagnostics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public final class FirebasePerformanceTrace extends PerformanceTrace {
    private Trace trace;

    @Override
    public void start(@Nonnull @NonNull @lombok.NonNull final String name) {
        if (this.trace == null) {
            this.trace = FirebasePerformance.getInstance().newTrace(name);
            this.trace.start();
        }
    }

    @Override
    public void stop() {
        if (this.trace != null) this.trace.stop();
    }
}
