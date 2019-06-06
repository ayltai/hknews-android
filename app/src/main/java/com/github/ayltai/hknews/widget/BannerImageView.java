package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.github.ayltai.hknews.R;

public final class BannerImageView extends ImageView {
    public BannerImageView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @LayoutRes
    @Override
    protected int getLayoutId() {
        return R.layout.view_image_banner;
    }
}
