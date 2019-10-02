package com.github.ayltai.hknews.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.ArrayUtils;
import org.tensorflow.lite.Interpreter;

import io.reactivex.Single;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.diagnostics.PerformanceTrace;
import com.github.ayltai.hknews.util.MathUtils;
import com.github.ayltai.hknews.util.MediaUtils;

@Singleton
public final class TensorFlowCenterFinder implements CenterFinder {
    private static Application application;

    private boolean isDisposed;

    public static void init(@Nonnull @NonNull @lombok.NonNull final Application application) {
        TensorFlowCenterFinder.application = application;
    }

    @Override
    public boolean isDisposed() {
        return this.isDisposed;
    }

    @Override
    public void dispose() {
        if (!this.isDisposed) this.isDisposed = true;
    }

    @Nonnull
    @NonNull
    @Override
    public Single<PointF> findCenter(@Nonnull @NonNull @lombok.NonNull final File file) {
        if (this.isDisposed) throw new IllegalStateException("This operation is not allowed after it is disposed");

        return Single.create(emitter -> {
            final PerformanceTrace trace = Components.getInstance().getDiagnosticsComponent().performanceTrace();
            trace.start(this.getClass().getSimpleName());

            final ByteBuffer buffer = ByteBuffer.allocateDirect(Constants.TFL_MODEL_WIDTH * Constants.TFL_MODEL_HEIGHT * 3 * 4).order(ByteOrder.nativeOrder());
            for (int pixel : MediaUtils.getPixels(file, MediaUtils.findDownSamplingScale(file), Constants.TFL_MODEL_WIDTH, Constants.TFL_MODEL_HEIGHT)) {
                buffer.putFloat((pixel >> (2 << 3) & 0xff) / 255f);
                buffer.putFloat((pixel >> (2 << 2) & 0xff) / 255f);
                buffer.putFloat((pixel             & 0xff) / 255f);
            }

            final float[][][][] tensor = new float[1][1][Constants.TFL_MODEL_HEIGHT >> 3][Constants.TFL_MODEL_WIDTH >> 3];
            try {
                this.runInterpreter(buffer, tensor);
            } catch (final IOException e) {
                if (!emitter.isDisposed()) emitter.onError(e);
            }

            if (!emitter.isDisposed()) emitter.onSuccess(TensorFlowCenterFinder.findLargestFocusArea(MathUtils.reshape(MathUtils.softMax(MathUtils.flatten(tensor[0][0]), Components.getInstance().getConfigComponent().remoteConfigurations().getTemperature()), Constants.TFL_MODEL_HEIGHT >> 3, Constants.TFL_MODEL_WIDTH >> 3)[0][0], Components.getInstance().getConfigComponent().remoteConfigurations().getLowerBound()));

            trace.stop();
        });
    }

    private void runInterpreter(@Nonnull @NonNull @lombok.NonNull final ByteBuffer buffer, @Nonnull @NonNull @lombok.NonNull final float[][][][] tensor) throws IOException {
        try (final AssetFileDescriptor descriptor = TensorFlowCenterFinder.application.getAssets().openFd("model.tflite")) {
            try (final FileInputStream inputStream = new FileInputStream(descriptor.getFileDescriptor())) {
                final Interpreter interpreter = new Interpreter(inputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, descriptor.getStartOffset(), descriptor.getDeclaredLength()), null);
                interpreter.run(buffer, tensor);
                interpreter.close();
            }
        }
    }

    private static PointF findLargestFocusArea(@Nonnull @NonNull @lombok.NonNull final float[][] heatMap, final float lowerBound) {
        final float            min        = lowerBound * Collections.min(Arrays.asList(ArrayUtils.toObject(MathUtils.flatten(heatMap))));
        final int[][]          cheatSheet = new int[heatMap.length][heatMap[0].length];
        final List<BinaryBlob> blobs      = new ArrayList<>();

        for (int i = 0; i < heatMap.length; i++) {
            for (int j = 0; j < heatMap[i].length; j++) {
                if (heatMap[i][j] >= min && cheatSheet[i][j] == 0) {
                    final BinaryBlob blob = new BinaryBlob();
                    blob.explore(new Point(i, j), heatMap, cheatSheet, min);
                    blobs.add(blob);
                }
            }
        }

        final BinaryBlob largestBlob = Collections.max(blobs, (blob1, blob2) -> Float.compare(blob1.getRelevance(), blob2.getRelevance()));

        if (blobs.size() >= 2 && blobs.size() <= 3) {
            for (int i = 0; i < blobs.size() - 1; i++) {
                final BinaryBlob blob = blobs.get(i);
                if (blob != largestBlob && MathUtils.distance(largestBlob.getCenter(), blob.getCenter()) <= 3f && blob.getRelevance() > largestBlob.getRelevance() * Constants.RELEVANCE_THRESHOLD) largestBlob.merge(blob);
            }
        }

        final PointF center = largestBlob.getCenter();
        return new PointF(center.x / heatMap[0].length, center.y / heatMap.length);
    }
}
