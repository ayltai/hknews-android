package com.github.ayltai.hknews.widget;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.data.model.Video;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.ItemPresenter;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public abstract class ItemView extends BaseView implements ItemPresenter.View {
    //region Subscriptions

    protected final FlowableProcessor<Irrelevant> clicks            = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> iconClicks        = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> sourceClicks      = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> publishDateClicks = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> titleClicks       = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> descriptionClicks = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> linkClicks        = PublishProcessor.create();
    protected final FlowableProcessor<Integer>    videoClicks       = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> bookmarkClicks    = PublishProcessor.create();

    //endregion

    protected View container;

    protected ItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    //region Properties

    @Override
    public void setIcon(@Nonnull @NonNull @lombok.NonNull final String iconUrl) {
    }

    @Override
    public void setTitle(@Nullable final CharSequence title) {
    }

    @Override
    public void setDescription(@Nullable final CharSequence description) {
    }

    @Override
    public void setSource(@Nullable final CharSequence source) {
    }

    @Override
    public void setPublishDate(@Nullable final Date date) {
    }

    @Override
    public void setLink(@Nullable final CharSequence link) {
    }

    @Override
    public void setImages(@Nonnull @NonNull @lombok.NonNull final List<Image> images) {
    }

    @Override
    public void setVideos(@Nonnull @NonNull @lombok.NonNull final List<Video> videos) {
    }

    @Override
    public void setIsBookmarked(final boolean isBookmarked) {
    }

    @Override
    public void setIsRead(final boolean isRead) {
    }

    //endregion

    //region Events

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> clicks() {
        return this.clicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> iconClicks() {
        return this.iconClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> sourceClicks() {
        return this.sourceClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> publishDateClicks() {
        return this.publishDateClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> titleClicks() {
        return this.titleClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> descriptionClicks() {
        return this.descriptionClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> linkClicks() {
        return this.linkClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Integer> videoClicks() {
        return this.videoClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> bookmarkClicks() {
        return this.bookmarkClicks;
    }

    //endregion

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (this.container != null) this.container.setOnClickListener(view -> this.clicks.onNext(Irrelevant.INSTANCE));
    }
}
