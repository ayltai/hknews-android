package com.github.ayltai.hknews.view;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.Components;

public final class PagerPresenter extends ModelPresenter<List<String>, PagerPresenter.View> {
    public interface View extends Presenter.View {
        void setCategoryNames(@Nonnull @NonNull @lombok.NonNull List<String> categoryNames);

        void refresh();

        void clear();

        void filter(@Nullable CharSequence text);
    }

    private boolean isInitialized;

    public void refresh() {
        if (this.getView() != null) this.getView().refresh();
    }

    public void clear() {
        if (this.getView() != null) this.getView().clear();
    }

    public void filter(@Nullable final CharSequence text) {
        if (this.getView() != null) this.getView().filter(text);
    }

    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null) this.getView().setCategoryNames(this.getModel());
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final PagerPresenter.View view) {
        super.onViewAttached(view);

        if (!this.isInitialized) {
            this.isInitialized = true;

            this.setModel(Components.getInstance()
                .getConfigComponent()
                .userConfigurations()
                .getCategoryNames());

            this.bindModel();
        }
    }
}
