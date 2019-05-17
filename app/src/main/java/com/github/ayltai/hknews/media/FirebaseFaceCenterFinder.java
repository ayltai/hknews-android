package com.github.ayltai.hknews.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.diagnostics.PerformanceTrace;
import com.github.ayltai.hknews.util.DevUtils;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

@Singleton
public final class FirebaseFaceCenterFinder implements FaceCenterFinder, Disposable {
    private final FirebaseVisionFaceDetector detector;

    private boolean isDisposed;

    FirebaseFaceCenterFinder(@Nonnull @NonNull @lombok.NonNull final FirebaseVisionFaceDetectorOptions options) {
        this.detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
    }

    @Override
    public boolean isDisposed() {
        return this.isDisposed;
    }

    @Override
    public void dispose() {
        if (!this.isDisposed) {
            this.isDisposed = true;

            try {
                this.detector.close();
            } catch (final IOException e) {
                if (DevUtils.isLoggable()) Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
    }

    @Nonnull
    @NonNull
    @Override
    public Single<PointF> findFaceCenter(@Nonnull @NonNull @lombok.NonNull final File file) {
        if (this.isDisposed) throw new IllegalStateException("This operation is not allowed after it is disposed");

        return Single.create(emitter -> {
            final PerformanceTrace trace = Components.getInstance().getDiagnosticsComponent().performanceTrace();
            trace.start(this.getClass().getSimpleName());

            final int scale = FirebaseFaceCenterFinder.findDownSamplingScale(file);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scale;

            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            this.detector
                .detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener(faces -> {
                    if (faces.isEmpty()) {
                        if (!emitter.isDisposed()) emitter.onSuccess(new PointF(bitmap.getWidth() / 2f, bitmap.getHeight() / 2f));
                    } else {
                        final Collection<PointF> centers = new ArrayList<>(faces.size());
                        for (final FirebaseVisionFace face : faces) centers.add(new PointF(face.getBoundingBox().exactCenterX(), face.getBoundingBox().exactCenterY()));

                        float sumX = 0f;
                        float sumY = 0f;

                        for (final PointF center : centers) {
                            sumX += center.x;
                            sumY += center.y;
                        }

                        if (!emitter.isDisposed()) emitter.onSuccess(new PointF(scale * sumX / centers.size(), scale * sumY / centers.size()));
                    }
                })
                .addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) emitter.onError(e);
                })
                .addOnCompleteListener(task -> {
                    trace.stop();

                    bitmap.recycle();
                });
        });
    }

    private static int findDownSamplingScale(@NonNull final File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int width  = options.outWidth;
        int height = options.outHeight;
        int scale  = 1;

        while (width * height > Components.getInstance().getConfigComponent().remoteConfigurations().getMaxImageSizeForFaceDetection()) {
            width  /= 2;
            height /= 2;
            scale  *= 2;
        }

        return scale;
    }
}
