package com.github.ayltai.hknews.util;

import java.util.Date;

import javax.annotation.Nonnull;

import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ayltai.hknews.Constants;
import com.github.piasy.biv.view.BigImageView;

public final class DataBindingUtils {
    private DataBindingUtils() {
    }

    @BindingAdapter("title")
    public static void setTitle(@Nonnull @NonNull @lombok.NonNull final TextView view, @Nullable final String title) {
        view.setText(title == null ? null : Html.fromHtml(title));
    }

    @BindingAdapter("description")
    public static void setDescription(@Nonnull @NonNull @lombok.NonNull final TextView view, @Nullable final String description) {
        view.setText(description == null ? null : Html.fromHtml(description));
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(@Nonnull @NonNull @lombok.NonNull final SimpleDraweeView view, @Nullable final String imageUrl) {
        if (imageUrl == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setImageURI(Constants.BASE_URL + imageUrl);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(@Nonnull @NonNull @lombok.NonNull final BigImageView view, @Nullable final String imageUrl) {
        if (imageUrl == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.showImage(Uri.parse(imageUrl));
        }
    }

    @BindingAdapter("publishDate")
    public static void setPublishDate(@Nonnull @NonNull @lombok.NonNull final TextView view, @Nullable final Date date) {
        view.setText(date == null ? null : DateUtils.getHumanReadableDate(view.getContext(), date));
    }
}
