package com.github.ayltai.hknews.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Event {
    protected final List<Attribute> attributes = new ArrayList<>();

    @Getter
    @Nonnull
    @NonNull
    private final String name;

    protected Collection<Attribute> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }

    @Nullable
    protected Attribute getAttribute(@Nonnull @NonNull @lombok.NonNull final String name) {
        for (final Attribute attribute : this.attributes) {
            if (name.equals(attribute.getName())) return attribute;
        }

        return null;
    }

    @NonNull
    protected Event addAttribute(@Nonnull @NonNull @lombok.NonNull final Attribute attribute) {
        this.attributes.add(attribute);
        return this;
    }
}
