package com.github.ayltai.hknews.data.view;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Single;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.config.UserConfigurations;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import lombok.Getter;
import lombok.Setter;

public class ListViewModel extends AndroidViewModel {
    protected final ItemLoader loader;

    @Getter
    @Setter
    private String  keywords;
    private boolean isForcedRefresh;

    public ListViewModel(@Nonnull @NonNull @lombok.NonNull final Application application) {
        super(application);

        this.loader = this.getLoader();
    }

    @Nonnull
    @NonNull
    public Single<List<Item>> getItems(@Nullable final String category) {
        final UserConfigurations configs = Components.getInstance()
            .getConfigComponent()
            .userConfigurations();

        this.isForcedRefresh = this.keywords == null && (this.isForcedRefresh || System.currentTimeMillis() - configs.getLastUpdatedDate(category).getTime() > Constants.AUTO_REFRESH_TIME);

        this.loader.setCategoryNames(Collections.singletonList(category));
        this.loader.setKeywords(this.keywords);
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
