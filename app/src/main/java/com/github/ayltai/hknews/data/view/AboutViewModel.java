package com.github.ayltai.hknews.data.view;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModel;

import lombok.Getter;
import lombok.Setter;

public final class AboutViewModel extends ViewModel {
    @Getter
    @Setter
    @StringRes
    private int logo;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    @StringRes
    private int description;

    @Getter
    @Setter
    @StringRes
    private int website;

    @Getter
    @Setter
    @StringRes
    private int websiteUrl;

    @Getter
    @Setter
    @StringRes
    private int issues;

    @Getter
    @Setter
    @StringRes
    private int issuesUrl;
}
