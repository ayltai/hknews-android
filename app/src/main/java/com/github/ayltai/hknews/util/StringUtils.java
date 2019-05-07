package com.github.ayltai.hknews.util;

import java.util.Arrays;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    public String join(@Nullable final CharSequence delimiter, @Nonnull @NonNull @lombok.NonNull final CharSequence... elements) {
        return StringUtils.join(delimiter, Arrays.asList(elements));
    }

    public String join(@Nullable final CharSequence delimiter, @Nonnull @NonNull @lombok.NonNull final Iterable<? extends CharSequence> elements) {
        final StringBuilder builder = new StringBuilder();

        for (final CharSequence element : elements) {
            if (builder.length() > 0 && delimiter != null) builder.append(delimiter);
            builder.append(element);
        }

        return builder.toString();
    }
}
