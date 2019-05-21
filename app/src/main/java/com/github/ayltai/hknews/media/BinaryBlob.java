package com.github.ayltai.hknews.media;

import javax.annotation.Nonnull;

import android.graphics.PointF;

import androidx.annotation.NonNull;

final class BinaryBlob {
    private int   pixelCount;
    private float centerX;
    private float centerY;
    private float weightedSum;

    public void addPixel(final int x, final int y, final float weight) {
        this.pixelCount++;

        this.centerX += x * weight;
        this.centerY += y * weight;

        this.weightedSum += weight;
    }

    public void explore(final int j, final int i, final float[][] heatMap, final int[][] cheatSheet, final float lowerBound) {
        if (i >= 0 && j >= 0 && heatMap.length > i && heatMap[i].length > j && heatMap[i][j] > lowerBound && cheatSheet[i][j] == 0) {
            this.addPixel(j, i, heatMap[i][j]);

            cheatSheet[i][j] = 1;

            this.explore(j + 1, i, heatMap, cheatSheet, lowerBound);
            this.explore(j - 1, i, heatMap, cheatSheet, lowerBound);
            this.explore(j, i + 1, heatMap, cheatSheet, lowerBound);
            this.explore(j, i - 1, heatMap, cheatSheet, lowerBound);
        }
    }

    public void merge(@Nonnull @NonNull @lombok.NonNull final BinaryBlob blob) {
        this.pixelCount  += blob.pixelCount;
        this.centerX     += blob.centerX;
        this.centerY     += blob.centerY;
        this.weightedSum += blob.weightedSum;
    }

    @Nonnull
    @NonNull
    public PointF getCenter() {
        return new PointF(this.centerX / this.weightedSum, this.centerY / this.weightedSum);
    }

    public float getRelevance() {
        return (float)Math.pow(this.weightedSum, 2.0) / this.pixelCount;
    }
}
