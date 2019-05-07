package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.VideoActivity;
import com.github.ayltai.hknews.media.BaseImageLoaderCallback;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.MediaUtils;
import com.github.ayltai.hknews.view.VideoPresenter;
import com.github.piasy.biv.view.BigImageView;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public final class VideoView extends BaseView implements VideoPresenter.View {
    protected final FlowableProcessor<Irrelevant> clicks = PublishProcessor.create();

    private final BigImageView image;

    public VideoView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_video, this, false);

        this.image = view.findViewById(R.id.image);
        this.image.setImageLoaderCallback(new BaseImageLoaderCallback() {
            @Override
            public void onFinish() {
                MediaUtils.configure(VideoView.this.image);
            }
        });

        this.addView(view);
    }

    @Override
    public void setImageUrl(@Nonnull @NonNull @lombok.NonNull final String imageUrl) {
        Log.d(this.getClass().getSimpleName(), "imageUrl = " + imageUrl);

        this.image.showImage(Uri.parse(imageUrl));
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> clicks() {
        return this.clicks;
    }

    @Override
    public void show(@NonNull @Nonnull @lombok.NonNull final String videoUrl) {
        VideoActivity.show(this.getContext(), videoUrl);
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        final View.OnClickListener onClickListener = view -> this.clicks.onNext(Irrelevant.INSTANCE);

        this.setOnClickListener(onClickListener);
        this.image.setOnClickListener(onClickListener);
    }
}
