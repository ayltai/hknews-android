package com.github.ayltai.hknews.data.loader;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import com.github.ayltai.hknews.data.model.Item;

public class LocalItemLoader extends ItemLoader {
    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Single.just(Collections.emptyList());
    }

    @Nonnull
    @NonNull
    @Override
    protected Consumer<? super List<Item>> onLoadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return items -> { };
    }

    @Override
    protected boolean isValid(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        return true;
    }
}
