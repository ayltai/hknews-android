package com.github.ayltai.hknews.view;

import androidx.annotation.UiThread;

public final class FeaturedItemPresenter<V extends ItemPresenter.View> extends ItemPresenter<V> {
    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() != null && this.getModel() != null) {
            this.getView().setTitle(this.getModel().getTitle());

            if (!this.getModel().getVideos().isEmpty()) {
                this.getView().setVideos(this.getModel().getVideos());
            } else if (!this.getModel().getImages().isEmpty()) {
                this.getView().setImages(this.getModel().getImages());
            }
        }
    }
}
