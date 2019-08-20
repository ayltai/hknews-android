package com.github.ayltai.hknews.data.loader;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Source;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.SourceRepository;
import com.github.ayltai.hknews.util.RxUtils;

public final class SourceLoader extends Loader<List<Source>> {
    @Nonnull
    @NonNull
    @Override
    protected Single<List<Source>> loadLocally(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return SourceRepository.create(context)
            .flatMap(SourceRepository::get)
            .compose(RxUtils.applySingleSchedulers(ItemRepository.SCHEDULER));
    }

    @Nonnull
    @NonNull
    @Override
    protected Single<List<Source>> loadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Components.getInstance()
            .getNetComponent()
            .apiService()
            .sources();
    }

    @Nonnull
    @NonNull
    @Override
    protected Consumer<? super List<Source>> onLoadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return sources -> { };
    }

    @Override
    protected boolean isValid(@Nonnull @NonNull @lombok.NonNull final List<Source> item) {
        return true;
    }
}
