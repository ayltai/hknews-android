package com.github.ayltai.hknews.config;

import java.util.Date;
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
    Date getLastUpdatedDate(@Nonnull @NonNull @lombok.NonNull String categoryName);

    void setLastUpdatedDate(@Nonnull @NonNull @lombok.NonNull String categoryName, @Nonnull @NonNull @lombok.NonNull Date date);

    int getPosition(@Nonnull @NonNull @lombok.NonNull String listName, @Nonnull @NonNull @lombok.NonNull String categoryName);

    void setPosition(@Nonnull @NonNull @lombok.NonNull String listName, @Nonnull @NonNull @lombok.NonNull String categoryName, int position);
}
