package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.model.Item;

import io.reactivex.Single;

public abstract class LocalListViewModel extends ListViewModel {
    protected LocalListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> getItems(@Nonnull @NonNull @lombok.NonNull final String category) {
        this.loader.setCategoryNames(Collections.singletonList(category));

        return this.loader.load(this.getApplication());
    }
}
