package com.github.ayltai.hknews.media;

import java.io.File;

import javax.annotation.Nonnull;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public interface CenterFinder extends Disposable {
    @Nonnull
    @NonNull
    Single<PointF> findCenter(@Nonnull @NonNull @lombok.NonNull File file);
}
