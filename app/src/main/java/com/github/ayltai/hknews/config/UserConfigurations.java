package com.github.ayltai.hknews.config;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

public interface UserConfigurations {
    boolean isDarkTheme();

    void setIsDarkTheme(boolean isDarkTheme);

    boolean isCompactStyle();

    void setIsCompactStyle(boolean isCompactStyle);

    @Nonnull
    @NonNull
    List<String> getSourceNames();

    void setSourceNames(@Nonnull @NonNull @lombok.NonNull List<String> sourceNames);

    @Nonnull
    @NonNull
    List<String> getCategoryNames();

    void setCategoryNames(@Nonnull @NonNull @lombok.NonNull List<String> categoryNames);

    @Nonnull
    @NonNull
    List<String> getSearchHistories();

    void setSearchHistories(@Nonnull @NonNull @lombok.NonNull List<String> searchHistories);
}
