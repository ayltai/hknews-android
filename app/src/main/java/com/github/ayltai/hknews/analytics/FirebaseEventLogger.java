package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.util.DevUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

@Singleton
public final class FirebaseEventLogger extends EventLogger {
    @Override
    protected void logEvent(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final ShareEvent event) {
        final Attribute source   = event.getAttribute(ShareEvent.ATTRIBUTE_SOURCE);
        final Attribute category = event.getAttribute(ShareEvent.ATTRIBUTE_CATEGORY);
        final Attribute url      = event.getAttribute(ShareEvent.ATTRIBUTE_URL);

        if (!DevUtils.isRunningTests() && source != null && category != null && url != null) {
            final Bundle bundle = new Bundle();

            FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SHARE, bundle);
        }
    }

    @Override
    protected void logCustomEvent(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final Event event) {
        final Bundle bundle = new Bundle();

        for (final Attribute attribute : event.getAttributes()) bundle.putString(attribute.getName(), attribute.getValue());

        if (!DevUtils.isRunningTests()) FirebaseAnalytics.getInstance(context).logEvent(event.getName(), bundle);
    }
}
