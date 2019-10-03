package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntentUtils {
    public void viewUrl(@Nullable final Activity activity, @Nonnull @NonNull @lombok.NonNull final String url) {
        IntentUtils.launchIntent(activity, new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void shareUrl(@Nullable final Activity activity, @Nonnull @NonNull @lombok.NonNull final String url, @Nonnull @NonNull @lombok.NonNull final String title) {
        IntentUtils.launchIntent(activity, new Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, url)
            .putExtra(Intent.EXTRA_SUBJECT, title));
    }

    private void launchIntent(@Nullable final Activity activity, @Nonnull @NonNull @lombok.NonNull final Intent intent) {
        if (activity != null && intent.resolveActivity(activity.getPackageManager()) != null) activity.startActivity(intent);
    }
}
