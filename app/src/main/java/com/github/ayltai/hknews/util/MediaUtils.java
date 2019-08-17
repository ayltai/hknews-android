package com.github.ayltai.hknews.util;

import java.io.File;

import javax.annotation.Nonnull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.piasy.biv.view.BigImageView;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MediaUtils {
    public void configure(@Nullable final BigImageView imageView) {
        if (imageView == null || imageView.getSSIV() == null) return;

        imageView.getSSIV().setMaxScale(Constants.MAX_ZOOM);
        imageView.getSSIV().setZoomEnabled(false);
        imageView.getSSIV().setPanEnabled(false);
    }

    public int findDownSamplingScale(@Nonnull @NonNull @lombok.NonNull final File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int width  = options.outWidth;
        int height = options.outHeight;
        int scale  = 1;

        while (width * height > Components.getInstance().getConfigComponent().remoteConfigurations().getMaxImageSize()) {
            width  /= 2;
            height /= 2;
            scale  *= 2;
        }

        return scale;
    }

    public int[] getPixels(@Nonnull @NonNull @lombok.NonNull final File file, final int scale, final int width, final int height) {
        final int[]  pixels       = new int[width * height];
        final Bitmap scaledBitmap = MediaUtils.decode(file, scale, width, height);

        scaledBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        scaledBitmap.recycle();

        return pixels;
    }

    public Bitmap decode(@Nonnull @NonNull @lombok.NonNull final File file, final int scale, final int width, final int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        final Bitmap bitmap       = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        bitmap.recycle();

        return scaledBitmap;
    }
}
