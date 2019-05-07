package com.github.ayltai.hknews.widget;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.media.BaseImageLoaderCallback;
import com.github.ayltai.hknews.util.DateUtils;
import com.github.ayltai.hknews.util.MediaUtils;
import com.github.piasy.biv.view.BigImageView;

public final class CompactItemView extends ItemView {
    private static final float ALPHA = 0.85f;

    //region Variables

    private final SimpleDraweeView icon;
    private final TextView         title;
    private final TextView         description;
    private final TextView         source;
    private final TextView         publishDate;
    private final BigImageView     image;

    //endregion

    public CompactItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_item_compact, this, false);

        this.container   = view.findViewById(R.id.container);
        this.icon        = view.findViewById(R.id.source_icon);
        this.title       = view.findViewById(R.id.title);
        this.description = view.findViewById(R.id.description);
        this.source      = view.findViewById(R.id.source_name);
        this.publishDate = view.findViewById(R.id.publish_date);
        this.image       = view.findViewById(R.id.image);

        final View.OnClickListener listener = v -> this.container.performClick();

        this.icon.setOnClickListener(listener);
        this.title.setOnClickListener(listener);
        this.description.setOnClickListener(listener);
        this.source.setOnClickListener(listener);
        this.publishDate.setOnClickListener(listener);

        this.image.setOnClickListener(listener);
        this.image.setImageLoaderCallback(new BaseImageLoaderCallback() {
            @Override
            public void onFinish() {
                MediaUtils.configure(CompactItemView.this.image);
            }
        });

        this.addView(view);
    }

    //region Properties

    @Override
    public void setIcon(@Nonnull @NonNull @lombok.NonNull final String iconUrl) {
        this.icon.setImageURI(Constants.BASE_URL + iconUrl.substring(1));
    }

    @Override
    public void setTitle(@Nullable final CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            this.title.setVisibility(View.GONE);
        } else {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(title.toString(), Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(title.toString()));
        }
    }

    @Override
    public void setDescription(@Nullable final CharSequence description) {
        if (TextUtils.isEmpty(description)) {
            this.description.setVisibility(View.GONE);
            this.title.setMaxLines(2);
        } else {
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(description.toString(), Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(description.toString()));
            this.title.setMaxLines(1);
        }
    }

    @Override
    public void setSource(@Nullable final CharSequence source) {
        this.source.setText(source);
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
        if (images.isEmpty()) {
            this.image.setVisibility(View.GONE);
        } else {
            this.image.setVisibility(View.VISIBLE);
            this.image.showImage(Uri.parse(images.get(0).getImageUrl()));
        }
    }

    @Override
    public void setIsRead(final boolean isRead) {
        this.container.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.icon.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.title.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.description.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.source.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.publishDate.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
        this.image.setAlpha(isRead ? CompactItemView.ALPHA : 1f);
    }

    //endregion
}
