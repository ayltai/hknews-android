package com.github.ayltai.hknews.analytics;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

public final class ShareEvent extends Event {
    //region Constants

    public static final String ATTRIBUTE_SOURCE   = "source";
    public static final String ATTRIBUTE_CATEGORY = "category";
    public static final String ATTRIBUTE_URL      = "url";

    //endregion

    public ShareEvent(@Nonnull @NonNull @lombok.NonNull final String name) {
        super(name);
    }

    @Nonnull
    @NonNull
    public ShareEvent setSource(@Nonnull @NonNull @lombok.NonNull final String source) {
        return (ShareEvent)this.addAttribute(new Attribute(ShareEvent.ATTRIBUTE_SOURCE, source));
    }

    @Nonnull
    @NonNull
    public ShareEvent setCategory(@Nonnull @NonNull @lombok.NonNull final String category) {
        return (ShareEvent)this.addAttribute(new Attribute(ShareEvent.ATTRIBUTE_CATEGORY, category));
    }

    @Nonnull
    @NonNull
    public ShareEvent setUrl(@Nonnull @NonNull @lombok.NonNull final String url) {
        return (ShareEvent)this.addAttribute(new Attribute(ShareEvent.ATTRIBUTE_URL, url));
    }
}
