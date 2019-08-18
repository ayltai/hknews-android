package com.github.ayltai.hknews.widget;

import java.io.File;

import javax.annotation.Nonnull;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import io.reactivex.disposables.Disposable;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.media.BaseImageLoaderCallback;
import com.github.ayltai.hknews.util.MediaUtils;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.piasy.biv.view.BigImageView;

public final class SmartCroppingImageView extends BigImageView {
    private static final LruCache<String, PointF> CACHE = new LruCache<>(Constants.CACHE_SIZE);

    private Disposable disposable;

    //region Constructors

    public SmartCroppingImageView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    public SmartCroppingImageView(@Nonnull @NonNull @lombok.NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartCroppingImageView(@Nonnull @NonNull @lombok.NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //endregion

    @CallSuper
    @Override
    public void showImage(final Uri thumbnail, final Uri uri) {
        this.dispose();

        this.setImageLoaderCallback(new BaseImageLoaderCallback() {
            @Override
            public void onStart() {
                MediaUtils.configure(SmartCroppingImageView.this);
            }

            @Override
            public void onSuccess(@Nonnull @NonNull @lombok.NonNull final File image) {
                SmartCroppingImageView.this.dispose();

                final PointF cache = SmartCroppingImageView.CACHE.get(image.getAbsolutePath());

                if (cache == null) {
                    SmartCroppingImageView.this.disposable = Components.getInstance()
                        .getMediaComponent()
                        .centerFinder()
                        .findCenter(image)
                        .compose(RxUtils.applySingleBackgroundToMainSchedulers())
                        .subscribe(
                            center -> {
                                SmartCroppingImageView.CACHE.put(image.getAbsolutePath(), center);

                                if (SmartCroppingImageView.this.getSSIV() == null) return;

                                if (center.x >= 0 && center.y >= 0) SmartCroppingImageView.this.getSSIV().setScaleAndCenter(SmartCroppingImageView.this.getSSIV().getScale(), center);
                            },
                            RxUtils::handleError
                        );
                } else if (cache.x >= 0 && cache.y >= 0) {
                    SmartCroppingImageView.this.getSSIV().setScaleAndCenter(SmartCroppingImageView.this.getSSIV().getScale(), cache);
                }
            }

            @Override
            public void onFinish() {
                MediaUtils.configure(SmartCroppingImageView.this);
            }
        });

        super.showImage(thumbnail, uri);
    }

    @CallSuper
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.dispose();
    }

    private void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
            this.disposable = null;
        }
    }
}
