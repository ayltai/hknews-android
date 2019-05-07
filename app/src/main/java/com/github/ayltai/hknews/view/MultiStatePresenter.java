package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

public final class MultiStatePresenter extends BasePresenter<MultiStatePresenter.View> {
    public enum State {
        EMPTY,
        LOADING,
        HIDDEN
    }

    public interface View extends Presenter.View {
        void showEmptyState(@DrawableRes int icon, @StringRes int title, @StringRes int description, @StringRes int action, @Nonnull @NonNull @lombok.NonNull android.view.View.OnClickListener listener);

        void showLoadingState();

        void hide();
    }

    private int                               icon;
    private int                               title;
    private int                               description;
    private int                               action;
    private android.view.View.OnClickListener listener;
    private MultiStatePresenter.State         state;

    public void setEmptyIcon(@DrawableRes final int icon) {
        this.icon = icon;
    }

    public void setEmptyTitle(@StringRes final int title) {
        this.title = title;
    }

    public void setEmptyDescription(@StringRes final int description) {
        this.description = description;
    }

    public void setEmptyAction(@StringRes final int action) {
        this.action = action;
    }

    public void setEmptyClickListener(@Nonnull @NonNull @lombok.NonNull final android.view.View.OnClickListener listener) {
        this.listener = listener;
    }

    @UiThread
    public void setState(@Nonnull @NonNull @lombok.NonNull final MultiStatePresenter.State state) {
        this.state = state;

        if (this.getView() != null) {
            if (state == MultiStatePresenter.State.EMPTY) {
                this.getView().showEmptyState(this.icon, this.title, this.description, this.action, this.listener);
            } else if (state == MultiStatePresenter.State.LOADING) {
                this.getView().showLoadingState();
            } else {
                this.getView().hide();
            }
        }
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final MultiStatePresenter.View view) {
        super.onViewAttached(view);

        if (this.state != null) this.setState(this.state);
    }
}
