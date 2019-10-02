package com.github.ayltai.hknews.util;

import javax.annotation.Nonnull;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.ArrayUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {
    public static float[] flatten(@Nonnull @NonNull @lombok.NonNull final float[][] array) {
        Float[] flattened = ArrayUtils.toObject(array[0]);

        for (final float[] floats : array) flattened = ArrayUtils.addAll(flattened, ArrayUtils.toObject(floats));

        return ArrayUtils.toPrimitive(flattened);
    }

    public static float[] softMax(@Nonnull @NonNull @lombok.NonNull final float[] array, final float temperature) {
        final float[] softMax = new float[array.length];

        float sum = 0;

        for (int i = 0; i < array.length; i++) {
            softMax[i] = (float)Math.exp(array[i] / temperature);
            sum       += softMax[i];
        }

        if (sum > 0) for (int i = 0; i < array.length; i++) softMax[i] /= sum;

        return softMax;
    }

    public static float[][][][] reshape(@Nonnull @NonNull @lombok.NonNull final float[] array, final int rows, final int columns) {
        final float[][][][] reshaped = new float[1][1][rows][columns];

        for (int i = 0; i < rows; i++) {
            if (columns >= 0) System.arraycopy(array, i * columns, reshaped[0][0][i], 0, columns);
        }

        return reshaped;
    }

    public static float distance(@Nonnull @NonNull @lombok.NonNull final PointF a, @Nonnull @NonNull @lombok.NonNull final PointF b) {
        return (float)Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}
