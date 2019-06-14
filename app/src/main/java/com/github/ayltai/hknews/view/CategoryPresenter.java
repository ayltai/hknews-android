package com.github.ayltai.hknews.view;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.Components;

import io.reactivex.Flowable;

public final class CategoryPresenter extends ModelPresenter<List<String>, CategoryPresenter.View> {
    public interface View extends Presenter.View {
        void setCategoryNames(@Nonnull @NonNull List<String> categoryNames);

        @Nonnull
        @NonNull
        Flowable<String> selects();
    }

    private boolean isInitialized;
    private String  categoryName;

    public CategoryPresenter() {
        this.setModel(Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getCategoryNames());

        this.categoryName = this.getModel().get(0);
    }

    @Nonnull
    @NonNull
    public String getCategoryName() {
        return this.categoryName;
    }

    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null) this.getView().setCategoryNames(this.getModel());
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final CategoryPresenter.View view) {
        super.onViewAttached(view);

        this.manageDisposable(view.selects().subscribe(categoryName -> this.categoryName = categoryName));

        if (!this.isInitialized) {
            this.isInitialized = true;

            this.bindModel();
        }
    }
}
