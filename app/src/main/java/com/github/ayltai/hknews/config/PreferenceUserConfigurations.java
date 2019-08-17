package com.github.ayltai.hknews.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.preference.PreferenceManager;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Source;

@Singleton
public final class PreferenceUserConfigurations implements UserConfigurations {
    //region Constants

    private static final String KEY_IS_DARK_THEME    = "IS_DARK_THEME";
    private static final String KEY_IS_COMPACT_STYLE = "IS_COMPACT_STYLE";
    private static final String KEY_SOURCE_NAMES     = "SOURCE_NAMES";
    private static final String KEY_CATEGORY_NAMES   = "CATEGORY_NAMES";

    //endregion

    private static PreferenceUserConfigurations instance;

    @Nonnull
    @NonNull
    private final Context context;

    @Nonnull
    @NonNull
    private final SharedPreferences preferences;

    static void init(@Nonnull @NonNull @lombok.NonNull final Context context) {
        if (PreferenceUserConfigurations.instance == null) PreferenceUserConfigurations.instance = new PreferenceUserConfigurations(context);
    }

    @Nonnull
    @NonNull
    static UserConfigurations getInstance() {
        if (PreferenceUserConfigurations.instance == null) throw new NullPointerException("Did you forget to call init()?");

        return PreferenceUserConfigurations.instance;
    }

    private PreferenceUserConfigurations(@Nonnull @NonNull @lombok.NonNull final Context context) {
        this.context     = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isDarkTheme() {
        return this.preferences.getBoolean(PreferenceUserConfigurations.KEY_IS_DARK_THEME, (this.context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
    }

    public void setIsDarkTheme(final boolean isDarkTheme) {
        this.preferences
            .edit()
            .putBoolean(PreferenceUserConfigurations.KEY_IS_DARK_THEME, isDarkTheme)
            .commit();
    }

    public boolean isCompactStyle() {
        return this.preferences.getBoolean(PreferenceUserConfigurations.KEY_IS_COMPACT_STYLE, false);
    }

    public void setIsCompactStyle(final boolean isCompactStyle) {
        this.preferences
            .edit()
            .putBoolean(PreferenceUserConfigurations.KEY_IS_COMPACT_STYLE, isCompactStyle)
            .commit();
    }

    @Nonnull
    @NonNull
    @Override
    public List<String> getSourceNames() {
        final List<String> defaultSourceNames = Arrays.asList(this.context.getResources().getStringArray(R.array.publisher_values));
        final List<String> sourceNames        = new ArrayList<>(this.preferences.getStringSet(PreferenceUserConfigurations.KEY_SOURCE_NAMES, new ArraySet<>(defaultSourceNames)));

        Collections.sort(sourceNames, (sourceName1, sourceName2) -> defaultSourceNames.indexOf(Source.getDisplayName(sourceName1)) - defaultSourceNames.indexOf(Source.getDisplayName(sourceName2)));

        return sourceNames;
    }

    @Override
    public void setSourceNames(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames) {
        this.preferences
            .edit()
            .putStringSet(PreferenceUserConfigurations.KEY_SOURCE_NAMES, new HashSet<>(sourceNames))
            .commit();
    }

    @Nonnull
    @NonNull
    @Override
    public List<String> getCategoryNames() {
        final List<String> defaultCategoryNames = Arrays.asList(this.context.getResources().getStringArray(R.array.category_values));
        final List<String> categoryNames        = new ArrayList<>(this.preferences.getStringSet(PreferenceUserConfigurations.KEY_CATEGORY_NAMES, new ArraySet<>(defaultCategoryNames)));

        Collections.sort(categoryNames, (categoryName1, categoryName2) -> defaultCategoryNames.indexOf(categoryName1) - defaultCategoryNames.indexOf(categoryName2));

        return categoryNames;
    }

    @Override
    public void setCategoryNames(@Nonnull @NonNull @lombok.NonNull final List<String> categoryNames) {
        this.preferences
            .edit()
            .putStringSet(PreferenceUserConfigurations.KEY_CATEGORY_NAMES, new HashSet<>(categoryNames))
            .commit();
    }

    @Nonnull
    @NonNull
    @Override
    public Date getLastUpdatedDate(@Nonnull @NonNull @lombok.NonNull final String categoryName) {
        return new Date(this.preferences.getLong(categoryName, System.currentTimeMillis()));
    }

    @Override
    public void setLastUpdatedDate(@Nonnull @NonNull @lombok.NonNull final String categoryName, @NonNull @Nonnull @lombok.NonNull final Date date) {
        this.preferences
            .edit()
            .putLong(categoryName, date.getTime())
            .commit();
    }
}
