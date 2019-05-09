package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Attribute {
    @Getter
    @Nonnull
    @NonNull
    private final String name;

    @Getter
    @Nullable
    private final String value;
}
