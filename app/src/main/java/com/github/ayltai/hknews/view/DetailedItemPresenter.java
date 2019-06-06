package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.ayltai.hknews.widget.DetailedItemView;

import io.reactivex.Flowable;

public final class DetailedItemPresenter extends ItemPresenter<DetailedItemPresenter.View> {
    public interface View extends ItemPresenter.View {
        @Nullable
        Flowable<Irrelevant> viewOnWebClicks();

        @Nullable
        Flowable<Irrelevant> shareClicks();

        void viewOnWeb(@Nonnull @NonNull @lombok.NonNull String url);

        void share(@Nonnull @NonNull @lombok.NonNull String title, @Nonnull @NonNull @lombok.NonNull String url);
    }

    public static final class Factory implements Presenter.Factory<DetailedItemPresenter, DetailedItemPresenter.View> {
        @Override
        public boolean isSupported(@Nonnull @NonNull @lombok.NonNull final Object key) {
            return key instanceof DetailedItemView.Key;
        }

        @NonNull
        @Nonnull
        @Override
        public DetailedItemPresenter createPresenter() {
            return new DetailedItemPresenter();
        }

        @NonNull
        @Nonnull
        @Override
        public DetailedItemPresenter.View createView(@Nonnull @NonNull @lombok.NonNull final Context context) {
            return new DetailedItemView(context);
        }
    }

    private boolean isInitialized;

    @UiThread
    @Override
    public void bindModel() {
        if (this.getView() == null) {
            this.isInitialized = false;
        } else {
            this.isInitialized = true;

            super.bindModel();
        }
    }

    @Override
    protected void onBookmarkClick() {
        this.getModel().setBookmarked(!this.getModel().isBookmarked());

        if (this.getView() != null) {
            this.getModel().setBookmarked(this.getModel().isBookmarked());
            this.getView().setIsBookmarked(this.getModel().isBookmarked());

            this.manageDisposable(this.saveModel().subscribe(
                irrelevant -> { },
                RxUtils::handleError
            ));
        }
    }

    private void onViewOnWebClick() {
        if (this.getView() != null) this.getView().viewOnWeb(this.getModel().getUrl());
    }

    private void onShareClick() {
        if (this.getView() != null) this.getView().share(this.getModel().getTitle(), this.getModel().getUrl());
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final DetailedItemPresenter.View view) {
        if (view.viewOnWebClicks() != null) this.manageDisposable(view.viewOnWebClicks().subscribe(irrelevant -> this.onViewOnWebClick()));
        if (view.shareClicks() != null) this.manageDisposable(view.shareClicks().subscribe(irrelevant -> this.onShareClick()));

        super.onViewAttached(view);

        if (!this.isInitialized) this.bindModel();
    }
}
