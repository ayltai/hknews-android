package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Single;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

public class ListViewModel extends AndroidViewModel {
    protected final ItemLoader loader;

    private boolean isForcedRefresh;

    public ListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);

        this.loader = this.getLoader();
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> getItems(@Nonnull @NonNull @lombok.NonNull final String category) {
        final boolean isForcedRefresh = this.isForcedRefresh;

        this.loader.setCategoryNames(Collections.singletonList(category));
        this.loader.setForcedRefresh(this.isForcedRefresh);

        return this.loader
            .load(this.getApplication())
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .flatMap(items -> isForcedRefresh
                ? Single.just(items)
                : ItemRepository.create(this.getApplication())
                .flatMap(repository -> repository.set(items))
                .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER)))
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .doOnSuccess(items -> this.isForcedRefresh = true);
    }

    @Nonnull
    @NonNull
    protected ItemLoader getLoader() {
        final ItemLoader loader = new ItemLoader();

        loader.setSourceNames(Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getSourceNames());

        loader.setDays(Constants.NEWS_DAYS);

        return loader;
    }
}
