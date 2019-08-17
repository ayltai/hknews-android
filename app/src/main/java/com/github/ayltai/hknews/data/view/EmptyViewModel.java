package com.github.ayltai.hknews.data.view;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModel;

import lombok.Getter;
import lombok.Setter;

public final class EmptyViewModel extends ViewModel {
    @Getter
    @Setter
    @DrawableRes
    private int icon;

    @Getter
    @Setter
    @StringRes
    private int title;

    @Getter
    @Setter
    @StringRes
    private int description;

    @Getter
    @Setter
    @StringRes
    private int action;
}
