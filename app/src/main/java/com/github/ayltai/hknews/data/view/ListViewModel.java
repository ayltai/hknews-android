package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.Date;
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
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.Irrelevant;
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
        this.loader.setCategoryNames(Collections.singletonList(category));
        this.loader.setForcedRefresh(this.isForcedRefresh);

        return this.loader
            .load(this.getApplication())
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .doOnSuccess(items -> {
                if (this.isForcedRefresh) Components.getInstance()
                    .getConfigComponent()
                    .userConfigurations()
                    .setLastUpdatedDate(category, new Date());

                this.isForcedRefresh = true;
            });
    }

    @Nonnull
    @NonNull
    public Single<Irrelevant> clear(@Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final String category) {
        return Single.just(Irrelevant.INSTANCE);
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
