package com.github.ayltai.hknews.media;

import java.io.File;

import javax.annotation.Nonnull;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public interface FaceCenterFinder extends Disposable {
    @Nonnull
    @NonNull
    Single<PointF> findFaceCenter(@Nonnull @NonNull @lombok.NonNull File file);
}
