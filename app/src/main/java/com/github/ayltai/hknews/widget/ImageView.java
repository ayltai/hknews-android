package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.media.BaseImageLoaderCallback;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.MediaUtils;
import com.github.ayltai.hknews.view.ImagePresenter;
import com.github.piasy.biv.view.BigImageView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class ImageView extends BaseView implements ImagePresenter.View {
    protected final FlowableProcessor<Irrelevant> clicks = PublishProcessor.create();

    private final BigImageView image;
    private final TextView description;

    public ImageView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(this.getLayoutId(), this, false);

        this.image = view.findViewById(R.id.image);
        this.image.setImageLoaderCallback(new BaseImageLoaderCallback() {
            @Override
            public void onFinish() {
                MediaUtils.configure(ImageView.this.image);
            }
        });

        this.description = view.findViewById(R.id.description);
        if (this.description != null) this.description.setMovementMethod(LinkMovementMethod.getInstance());

        this.addView(view);
    }

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.view_image;
    }

    @Override
    public void setImageUrl(@Nullable final String imageUrl) {
        if (imageUrl == null) {
            this.image.setVisibility(View.GONE);
        } else {
            this.image.setVisibility(View.VISIBLE);
            this.image.showImage(Uri.parse(imageUrl));
        }
    }

    @Override
    public void setDescription(@Nullable final String description) {
        if (this.description != null) {
            if (TextUtils.isEmpty(description)) {
                this.description.setVisibility(View.GONE);
            } else {
                this.description.setVisibility(View.VISIBLE);
                this.description.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(description));
            }
        }
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> clicks() {
        return this.clicks;
    }

    @Override
    public void show(@Nonnull @NonNull @lombok.NonNull final String imageUrl) {
        new ImageViewer.Builder<>(this.getContext(), new String[] { imageUrl })
            .allowSwipeToDismiss(false)
            .show();
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        final OnClickListener onClickListener = view -> this.clicks.onNext(Irrelevant.INSTANCE);

        this.setOnClickListener(onClickListener);
        if (this.description != null) this.description.setOnClickListener(onClickListener);
        this.image.setOnClickListener(onClickListener);
    }
}
