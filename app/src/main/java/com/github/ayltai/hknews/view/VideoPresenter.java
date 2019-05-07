package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.data.model.Video;
import com.github.ayltai.hknews.util.Irrelevant;

import io.reactivex.Flowable;

public final class VideoPresenter extends ModelPresenter<Video, VideoPresenter.View> {
    public interface View extends Presenter.View {
        void setImageUrl(@Nonnull @NonNull @lombok.NonNull String imageUrl);

        @Nonnull
        @NonNull
        Flowable<Irrelevant> clicks();

        void show(@Nonnull @NonNull @lombok.NonNull String videoUrl);
    }

    @CallSuper
    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null && this.getModel() != null) {
            this.getView().setImageUrl(this.getModel().getImageUrl());
        }
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final VideoPresenter.View view) {
        if (this.getModel() != null) this.manageDisposable(view.clicks().subscribe(irrelevant -> view.show(this.getModel().getVideoUrl())));

        super.onViewAttached(view);
    }
}
