package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Image;
import com.github.ayltai.hknews.data.model.Video;
import com.github.ayltai.hknews.util.DeviceUtils;
import com.github.piasy.biv.view.BigImageView;

public final class FeaturedItemView extends ItemView {
    private final TextView     description;
    private final View         play;
    private final BigImageView image;

    public FeaturedItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_item_featured, this, false);

        this.container   = view.findViewById(R.id.container);
        this.description = view.findViewById(R.id.description);
        this.play        = view.findViewById(R.id.play);
        this.image       = view.findViewById(R.id.image);

        this.description.setOnTouchListener((v, e) -> this.container.onTouchEvent(e));
        this.play.setOnTouchListener((v, e) -> this.container.onTouchEvent(e));
        this.image.setOnClickListener(v -> this.container.performClick());

        this.addView(view);

        this.setLayoutParams(new FrameLayout.LayoutParams(DeviceUtils.getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setTitle(@Nullable final CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            this.description.setVisibility(View.GONE);
        } else {
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(title.toString(), Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(title.toString()));
        }
    }

    @Override
    public void setImages(@Nonnull @NonNull @lombok.NonNull final List<Image> images) {
        this.image.showImage(Uri.parse(images.get(0).getImageUrl()));

        this.play.setVisibility(View.GONE);
    }

    @Override
    public void setVideos(@Nonnull @NonNull @lombok.NonNull final List<Video> videos) {
        this.image.showImage(Uri.parse(videos.get(0).getImageUrl()));

        this.play.setVisibility(View.VISIBLE);
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.image.cancel();
    }
}
