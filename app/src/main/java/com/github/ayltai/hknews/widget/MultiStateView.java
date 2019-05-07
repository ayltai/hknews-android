package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.config.DaggerConfigComponent;
import com.github.ayltai.hknews.util.AnimationUtils;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.view.MultiStatePresenter;

public final class MultiStateView extends BaseView implements MultiStatePresenter.View, Cacheable {
    //region Components

    private final View      loading;
    private final View      empty;
    private final ImageView icon;
    private final TextView  title;
    private final TextView  description;
    private final Button    action;

    //endregion

    public MultiStateView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(DaggerConfigComponent.create().userConfigurations().isCompactStyle() ? R.layout.view_multi_state_compact : R.layout.view_multi_state_cozy, this, false);

        this.loading     = view.findViewById(R.id.loading);
        this.empty       = view.findViewById(R.id.empty);
        this.icon        = view.findViewById(R.id.empty_icon);
        this.title       = view.findViewById(R.id.empty_title);
        this.description = view.findViewById(R.id.empty_description);
        this.action      = view.findViewById(R.id.empty_action);

        this.addView(view);
    }

    @Override
    public void showEmptyState(@DrawableRes final int icon, @StringRes final int title, @StringRes final int description, @StringRes final int action, @Nonnull @NonNull @lombok.NonNull final View.OnClickListener listener) {
        this.icon.setImageResource(icon);
        this.title.setText(title);
        this.description.setText(description);
        this.action.setText(action);
        this.action.setOnClickListener(listener);

        this.loading.setVisibility(View.GONE);
        this.empty.setVisibility(View.VISIBLE);

        AnimationUtils.stopShimmerAnimation(this.loading);
    }

    @Override
    public void showLoadingState() {
        this.loading.setVisibility(View.VISIBLE);
        this.empty.setVisibility(View.GONE);

        AnimationUtils.startShimmerAnimation(this.loading);
    }

    @Override
    public void hide() {
        this.loading.setVisibility(View.GONE);
        this.empty.setVisibility(View.GONE);

        AnimationUtils.stopShimmerAnimation(this.loading);
    }
}
