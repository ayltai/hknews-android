package com.github.ayltai.hknews.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.model.Video;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.DevUtils;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.ayltai.hknews.widget.DetailedItemView;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import flow.Flow;

public class ItemPresenter<V extends ItemPresenter.View> extends ModelPresenter<Item, V> {
    public interface View extends Presenter.View {
        @UiThread
        void setIcon(@Nonnull @NonNull @lombok.NonNull String iconUrl);

        @UiThread
        void setSource(@Nullable CharSequence source);

        @UiThread
        void setPublishDate(@Nullable Date date);

        @UiThread
        void setTitle(@Nullable CharSequence title);

        @UiThread
        void setDescription(@Nullable CharSequence description);

        @UiThread
        void setLink(@Nullable CharSequence link);

        @UiThread
        void setImages(@Nonnull @NonNull @lombok.NonNull List<Image> images);

        @UiThread
        void setVideos(@Nonnull @NonNull @lombok.NonNull List<Video> videos);

        @UiThread
        void setIsRead(boolean isRead);

        @UiThread
        void setIsBookmarked(boolean isBookmarked);

        @Nullable
        Flowable<Irrelevant> clicks();

        @Nullable
        Flowable<Irrelevant> iconClicks();

        @Nullable
        Flowable<Irrelevant> sourceClicks();

        @Nullable
        Flowable<Irrelevant> publishDateClicks();

        @Nullable
        Flowable<Irrelevant> titleClicks();

        @Nullable
        Flowable<Irrelevant> descriptionClicks();

        @Nullable
        Flowable<Irrelevant> linkClicks();

        @Nullable
        Flowable<Integer> videoClicks();

        @Nullable
        Flowable<Irrelevant> bookmarkClicks();
    }

    @UiThread
    @CallSuper
    @Override
    public void bindModel() {
        if (this.getView() != null && this.getModel() != null) {
            this.getView().setIsRead(this.getModel().getLastAccessed() != null);
            this.getView().setIcon(this.getModel().getSource().getImageUrl());
            this.getView().setSource(this.getModel().getSource().getDisplayName());
            this.getView().setPublishDate(this.getModel().getPublishDate());
            this.getView().setTitle(this.getModel().getTitle());
            this.getView().setDescription(this.getModel().getDescription());
            this.getView().setLink(this.getModel().getUrl());
            this.getView().setIsBookmarked(this.getModel().isBookmarked());
            this.getView().setImages(this.getModel().getImages());
            this.getView().setVideos(this.getModel().getVideos());
        }
    }

    @CallSuper
    @Override
    public void onViewAttached(@NonNull final V view) {
        super.onViewAttached(view);

        if (view.clicks() != null) this.manageDisposable(view.clicks().subscribe(irrelevant -> this.onClick()));
        if (view.iconClicks() != null) this.manageDisposable(view.iconClicks().subscribe(irrelevant -> {
            this.onClick();
            this.onIconClick();
        }));
        if (view.sourceClicks() != null) this.manageDisposable(view.sourceClicks().subscribe(irrelevant -> {
            this.onClick();
            this.onSourceClick();
        }));
        if (view.publishDateClicks() != null) this.manageDisposable(view.publishDateClicks().subscribe(irrelevant -> {
            this.onClick();
            this.onPublishDateClick();
        }));
        if (view.titleClicks() != null) this.manageDisposable(view.titleClicks().subscribe(irrelevant -> {
            this.onClick();
            this.onTitleClick();
        }));
        if (view.descriptionClicks() != null) this.manageDisposable(view.descriptionClicks().subscribe(irrelevant -> {
            this.onClick();
            this.onDescriptionClick();
        }));
        if (view.linkClicks() != null) this.manageDisposable(view.linkClicks().subscribe(irrelevant -> {
            this.onLinkClick();
        }));
        if (view.bookmarkClicks() != null) this.manageDisposable(view.bookmarkClicks().subscribe(irrelevant -> {
            this.onBookmarkClick();
        }));
        if (view.videoClicks() != null) this.manageDisposable(view.videoClicks().subscribe(index -> {
            this.onClick();
            this.onVideoClick(this.getModel().getVideos().get(index));
        }));
    }

    @CallSuper
    protected void onClick() {
        if (this.getView() != null) {
            this.getModel().setLastAccessed(new Date());
            this.getView().setIsRead(true);

            this.manageDisposable(this.saveModel().subscribe(
                irrelevant -> {
                    if (!DevUtils.isRunningUnitTest()) Flow.get(this.getView().getContext()).set(DetailedItemView.Key.create(this.getModel()));
                },
                RxUtils::handleError
            ));
        }
    }

    protected void onIconClick() {
    }

    protected void onSourceClick() {
    }

    protected void onPublishDateClick() {
    }

    protected void onTitleClick() {
    }

    protected void onDescriptionClick() {
    }

    protected void onLinkClick() {
    }

    protected void onBookmarkClick() {
    }

    protected void onVideoClick(@Nonnull @NonNull @lombok.NonNull final Video video) {
    }

    @Nonnull
    @NonNull
    protected Single<Irrelevant> saveModel() {
        if (this.getView() == null) return Single.just(Irrelevant.INSTANCE);

        return ItemRepository.create(this.getView().getContext())
            .flatMap(repository -> repository.set(this.getModel()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Repository.SCHEDULER);
    }
}
