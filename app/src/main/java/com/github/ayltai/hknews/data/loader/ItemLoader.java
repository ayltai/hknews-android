package com.github.ayltai.hknews.data.loader;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import lombok.Getter;
import lombok.Setter;

public class ItemLoader extends Loader<List<Item>> {
    private static final String DELIMITER = ",";

    @Getter
    @Setter
    protected List<String> sourceNames;

    @Getter
    @Setter
    protected List<String> categoryNames;

    @Getter
    @Setter
    protected int days;

    @Getter
    @Setter
    protected String keywords;

    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadLocally(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return ItemRepository.create(context)
            .flatMap(repository -> repository.get(this.sourceNames, this.categoryNames, this.keywords));
    }

    @Nonnull
    @NonNull
    @Override
    protected Single<List<Item>> loadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return Components.getInstance()
            .getNetComponent()
            .apiService()
            .query(StringUtils.join(this.sourceNames, ItemLoader.DELIMITER), StringUtils.join(this.categoryNames, ItemLoader.DELIMITER), this.days);
    }

    @Nonnull
    @NonNull
    @Override
    protected Consumer<? super List<Item>> onLoadRemotely(@Nonnull @NonNull @lombok.NonNull final Context context) {
        return items -> ItemRepository.create(
            Components.getInstance()
                .getDataComponent(context)
                .realm())
            .set(items)
            .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
            .subscribe();
    }

    @Override
    protected boolean isValid(@Nonnull @NonNull @lombok.NonNull final List<Item> item) {
        return !item.isEmpty();
    }
}
