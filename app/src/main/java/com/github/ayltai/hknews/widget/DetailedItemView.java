package com.github.ayltai.hknews.widget;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.model.Video;
import com.github.ayltai.hknews.util.DateUtils;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.DetailedItemPresenter;
import com.github.ayltai.hknews.view.ImagePresenter;
import com.github.ayltai.hknews.view.VideoPresenter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.auto.value.AutoValue;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

import flow.Flow;

public final class DetailedItemView extends ItemView implements DetailedItemPresenter.View {
    @AutoValue
    public abstract static class Key extends ModelKey<Item> implements Parcelable {
        @Nonnull
        @NonNull
        public abstract Item getModel();

        @Nonnull
        @NonNull
        public static DetailedItemView.Key create(@Nonnull @NonNull @lombok.NonNull final Item model) {
            return new AutoValue_DetailedItemView_Key(model);
        }
    }

    //region Subscriptions

    private final FlowableProcessor<Irrelevant> viewOnWebClicks = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant> shareClicks     = PublishProcessor.create();

    //endregion

    //region Components

    private final CollapsingToolbarLayout collapsingToolbarLayout;
    private final ViewGroup               imageContainer;
    private final SimpleDraweeView        sourceIcon;
    private final TextView                sourceName;
    private final TextView                publishDate;
    private final TextView                title;
    private final TextView                description;
    private final ImageView               bookmarkButton;
    private final ImageView               viewOnWebButton;
    private final ImageView               shareButton;
    private final ViewGroup               imagesContainer;
    private final ViewGroup               videosContainer;

    private final ImagePresenter    featuredImagePresenter;
    private final BannerImageView bannerImageView;

    //endregion

    public DetailedItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_details, this, true);

        this.collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        this.imageContainer          = view.findViewById(R.id.image_container);
        this.sourceIcon              = view.findViewById(R.id.avatar);
        this.sourceName              = view.findViewById(R.id.source);
        this.publishDate             = view.findViewById(R.id.publish_date);
        this.title                   = view.findViewById(R.id.title);
        this.description             = view.findViewById(R.id.description);
        this.bookmarkButton          = view.findViewById(R.id.action_bookmark);
        this.viewOnWebButton         = view.findViewById(R.id.action_view_on_web);
        this.shareButton             = view.findViewById(R.id.action_share);
        this.imagesContainer         = view.findViewById(R.id.images_container);
        this.videosContainer         = view.findViewById(R.id.videos_container);

        this.title.setMovementMethod(LinkMovementMethod.getInstance());
        this.description.setMovementMethod(LinkMovementMethod.getInstance());

        this.featuredImagePresenter = new ImagePresenter();
        this.bannerImageView = new BannerImageView(context);
        this.imageContainer.addView(this.bannerImageView);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> Flow.get(v).goBack());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) ((CollapsingToolbarLayout)view.findViewById(R.id.collapsingToolbarLayout)).setExpandedTitleTextAppearance(R.style.TransparentText);

        this.updateLayout(BaseView.LAYOUT_SCREEN);
    }

    //region Properties

    @Override
    public void setIcon(@Nonnull @NonNull @lombok.NonNull final String iconUrl) {
        this.sourceIcon.setImageURI(Constants.BASE_URL + iconUrl.substring(1));
    }

    @Override
    public void setTitle(@Nullable final CharSequence title) {
        this.collapsingToolbarLayout.setTitle(title);

        if (TextUtils.isEmpty(title)) {
            this.featuredImagePresenter.setModel(null);
            this.featuredImagePresenter.bindModel();
        } else {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(title.toString(), Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(title.toString()));
        }
    }

    @Override
    public void setDescription(@Nullable final CharSequence description) {
        if (TextUtils.isEmpty(description)) {
            this.description.setVisibility(View.GONE);
        } else {
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(description.toString(), Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(description.toString()));
        }
    }

    @Override
    public void setSource(@Nullable final CharSequence source) {
        if (TextUtils.isEmpty(source)) {
            this.sourceName.setVisibility(View.GONE);
        } else {
            this.sourceName.setVisibility(View.VISIBLE);
            this.sourceName.setText(source);
        }
    }

    @Override
    public void setPublishDate(@Nullable final Date date) {
        if (date == null) {
            this.publishDate.setVisibility(View.GONE);
        } else {
            this.publishDate.setVisibility(View.VISIBLE);
            this.publishDate.setText(DateUtils.getHumanReadableDate(this.getContext(), date));
        }
    }

    @Override
    public void setImages(@Nonnull @NonNull @lombok.NonNull final List<Image> images) {
        this.imagesContainer.removeAllViews();

        if (images.isEmpty()) {
            this.imageContainer.setVisibility(View.GONE);
        } else {
            this.imageContainer.setVisibility(TextUtils.isEmpty(this.title.getText()) ? View.GONE : View.VISIBLE);

            this.featuredImagePresenter.setModel(images.get(0));
            this.featuredImagePresenter.bindModel();

            if (images.size() > 1) {
                for (final Image image : images) {
                    final ImagePresenter                            presenter = new ImagePresenter();
                    final com.github.ayltai.hknews.widget.ImageView view      = new com.github.ayltai.hknews.widget.ImageView(this.getContext());

                    presenter.setModel(image);

                    this.imagesContainer.addView(view);

                    presenter.onViewAttached(view);
                    presenter.bindModel();
                }
            }
        }
    }

    @Override
    public void setVideos(@Nonnull @NonNull @lombok.NonNull final List<Video> videos) {
        this.videosContainer.removeAllViews();

        for (final Video video : videos) {
            final VideoPresenter presenter = new VideoPresenter();
            final VideoView      view      = new VideoView(this.getContext());

            presenter.setModel(video);

            this.videosContainer.addView(view);

            presenter.onViewAttached(view);
            presenter.bindModel();
        }
    }

    @Override
    public void setIsBookmarked(final boolean isBookmarked) {
        this.bookmarkButton.setImageResource(isBookmarked ? R.drawable.ic_bookmark_filled_black_24dp : R.drawable.ic_bookmark_black_24dp);
    }

    //endregion

    //region Events

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

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> viewOnWebClicks() {
        return this.viewOnWebClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> shareClicks() {
        return this.shareClicks;
    }

    //endregion

    @Override
    public void viewOnWeb(@Nonnull @NonNull @lombok.NonNull final String url) {
        this.getContext().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), this.getContext().getString(R.string.view_on_web)));
    }

    @Override
    public void share(@Nonnull @NonNull @lombok.NonNull final String title, @Nonnull @NonNull @lombok.NonNull final String url) {
        this.getContext().startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_SUBJECT, title).putExtra(Intent.EXTRA_TEXT, url), this.getContext().getString(R.string.share_via)));
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        this.sourceIcon.setOnClickListener(view -> this.iconClicks.onNext(Irrelevant.INSTANCE));
        this.sourceName.setOnClickListener(view -> this.sourceClicks.onNext(Irrelevant.INSTANCE));
        this.bookmarkButton.setOnClickListener(view -> this.bookmarkClicks.onNext(Irrelevant.INSTANCE));
        this.viewOnWebButton.setOnClickListener(view -> this.viewOnWebClicks.onNext(Irrelevant.INSTANCE));
        this.shareButton.setOnClickListener(view -> this.shareClicks.onNext(Irrelevant.INSTANCE));

        this.featuredImagePresenter.onViewAttached(this.bannerImageView);

        super.onAttachedToWindow();
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        this.featuredImagePresenter.onViewDetached();

        super.onDetachedFromWindow();
    }
}
