package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.util.Irrelevant;

import io.reactivex.Flowable;

public final class ImagePresenter extends ModelPresenter<Image, ImagePresenter.View> {
    public interface View extends Presenter.View {
        void setImageUrl(@Nullable String imageUrl);

        void setDescription(@Nullable String description);

        @Nonnull
        @NonNull
        Flowable<Irrelevant> clicks();

        void show(@Nonnull @NonNull @lombok.NonNull String imageUrl);
    }

    @CallSuper
    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null) {
            if (this.getModel() == null) {
                this.getView().setImageUrl(null);
                this.getView().setDescription(null);
            } else {
                this.getView().setImageUrl(this.getModel().getImageUrl());
                this.getView().setDescription(this.getModel().getDescription());
            }
        }
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final ImagePresenter.View view) {
        this.manageDisposable(view.clicks().subscribe(irrelevant -> {
            if (this.getModel() != null) view.show(this.getModel().getImageUrl());
        }));

        super.onViewAttached(view);
    }
}
