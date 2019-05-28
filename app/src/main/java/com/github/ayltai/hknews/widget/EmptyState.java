package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.SettingsActivity;
import com.github.ayltai.hknews.view.Presenter;

public class EmptyState {
    @DrawableRes
    public int getIcon() {
        return R.drawable.ic_empty_news_black_24dp;
    }

    @StringRes
    public int getTitle() {
        return R.string.empty_news_title;
    }

    @StringRes
    public int getDescription() {
        return R.string.empty_news_description;
    }

    @StringRes
    public int getAction() {
        return R.string.empty_news_action;
    }

    @Nonnull
    @NonNull
    public View.OnClickListener getClickListener(@Nonnull @NonNull @lombok.NonNull final Presenter.View view) {
        return v -> {
            final Activity activity = view.getActivity();
            if (activity == null) {
                SettingsActivity.show(view.getContext());
            } else {
                SettingsActivity.show(activity);
            }
        };
    }
}
