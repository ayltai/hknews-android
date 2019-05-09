package com.github.ayltai.hknews.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.data.loader.ItemLoader;
import com.github.ayltai.hknews.data.loader.Loader;
import com.github.ayltai.hknews.data.model.Category;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ListPresenter extends ModelPresenter<List<Item>, ListPresenter.View> {
    public interface View extends Presenter.View {
        @Nonnull
        @NonNull
        Flowable<Irrelevant> refreshes();

        @Nonnull
        @NonNull
        Flowable<Irrelevant> clears();

        void update();

        void clear();

        void showEmptyView();

        void hideEmptyView();

        void showLoadingView();

        void hideLoadingView();
    }

    private final List<String> sourceNames;
    private final String       categoryName;

    public ListPresenter(@Nonnull @NonNull @lombok.NonNull final String categoryName) {
        this.sourceNames = Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getSourceNames();

        this.categoryName = categoryName;
    }

    public void filter(@Nullable final CharSequence text) {
        this.load(this.createItemLoader(), this.sourceNames, this.categoryName, text, false);
    }

    @UiThread
    @Override
    public void bindModel() {
        this.load(this.createItemLoader(), this.sourceNames, this.categoryName, null, false);
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final ListPresenter.View view) {
        super.onViewAttached(view);

        this.subscribeRefreshes(view);
        this.subscribeClears(view);
    }

    @Nonnull
    @NonNull
    protected Single<Irrelevant> refresh() {
        if (this.getView() == null) return Single.just(Irrelevant.INSTANCE);

        return Single.defer(() -> {
            this.load(this.createItemLoader(), this.sourceNames, this.categoryName, null, true);

            return Single.just(Irrelevant.INSTANCE);
        });
    }

    @Nonnull
    @NonNull
    protected Single<Irrelevant> clear() {
        return Single.just(Irrelevant.INSTANCE);
    }

    @Nonnull
    @NonNull
    protected ItemLoader createItemLoader() {
        final ItemLoader loader = new ItemLoader();
        loader.setDays(Constants.NEWS_DAYS);

        return loader;
    }

    private void load(@Nonnull @NonNull @lombok.NonNull final ItemLoader loader, @Nonnull @NonNull @lombok.NonNull final List<String> sourceNames, @Nonnull @NonNull @lombok.NonNull final String categoryName, @Nullable final CharSequence text, final boolean isForcedRefresh) {
        if (this.getView() != null) {
            loader.setSourceNames(sourceNames);
            loader.setCategoryNames(Arrays.asList(Category.getDisplayName(categoryName), Category.REAL_TIME + Category.getDisplayName(categoryName)));
            loader.setKeywords(text == null ? null : text.toString());
            loader.setForcedRefresh(isForcedRefresh);

            this.load(this.getView().getContext(), loader);
        }
    }

    private void subscribeRefreshes(@Nonnull @NonNull @lombok.NonNull final ListPresenter.View view) {
        this.manageDisposable(view.refreshes()
            .flatMap(irrelevant -> this.refresh().toFlowable())
            .compose(RxUtils.applyFlowableBackgroundToMainSchedulers())
            .subscribe(
                irrelevant -> { },
                RxUtils::handleError
            ));


    }

    private void subscribeClears(@Nonnull @NonNull @lombok.NonNull final ListPresenter.View view) {
        this.manageDisposable(view.clears()
            .flatMap(irrelevant -> this.clear().toFlowable())
            .compose(RxUtils.applyFlowableBackgroundToMainSchedulers())
            .subscribe(
                irrelevant -> {
                    this.setModel(Collections.emptyList());
                    this.bindModel();
                },
                RxUtils::handleError
            ));
    }

    private void load(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final Loader<List<Item>> loader) {
        if (this.getView() != null) {
            this.getView().showLoadingView();

            this.manageDisposable(loader.load(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    items -> {
                        this.setModel(items);

                        if (items.isEmpty()) {
                            this.getView().showEmptyView();
                        } else {
                            this.getView().hideEmptyView();
                        }

                        this.getView().update();
                    },
                    RxUtils::handleError
                ));
        }
    }
}
