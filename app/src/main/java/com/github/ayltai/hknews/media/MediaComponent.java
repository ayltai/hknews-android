package com.github.ayltai.hknews.media;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

import com.github.piasy.biv.loader.ImageLoader;

import dagger.Component;

@Singleton
@Component(modules = { MediaModule.class })
public interface MediaComponent {
    @Nonnull
    @NonNull
    ImageLoader imageLoader();

    @Nonnull
    @NonNull
    FaceCenterFinder faceCenterFinder();
}
