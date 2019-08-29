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

public class LocalListViewModel extends ListViewModel {
    public LocalListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);
    }

    @Nonnull
    @NonNull
    @Override
    public Single<List<Item>> getItems(@Nullable final String category) {
        this.loader.setCategoryNames(category == null ? Collections.emptyList() : Collections.singletonList(category));
        this.loader.setKeywords(this.getKeywords());
        this.loader.setForcedRefresh(false);

        return this.loader
            .load(this.getApplication())
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER));
    }
}
