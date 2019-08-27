package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Single;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

public abstract class LocalListViewModel extends ListViewModel {
    protected LocalListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    @Override
    public Single<List<Item>> getItems(@Nonnull @NonNull @lombok.NonNull final String category, @Nullable final String keywords) {
        this.loader.setCategoryNames(Collections.singletonList(category));
        this.loader.setKeywords(keywords);

        return this.loader
            .load(this.getApplication())
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
