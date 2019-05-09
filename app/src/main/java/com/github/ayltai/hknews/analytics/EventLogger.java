package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.util.DevUtils;

public abstract class EventLogger {
    public <T extends Event> void logEvent(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final T event) {
        if (!DevUtils.isLoggable() && !DevUtils.isRunningTests()) {
            if (event instanceof ShareEvent) {
                this.logEvent(context, (ShareEvent)event);
            } else {
                this.logCustomEvent(context, event);
            }
        }
    }

    protected abstract void logEvent(@Nonnull @NonNull @lombok.NonNull Context context, @Nonnull @NonNull @lombok.NonNull ShareEvent event);

    protected abstract void logCustomEvent(@Nonnull @NonNull @lombok.NonNull Context context, @Nonnull @NonNull @lombok.NonNull Event event);
}
