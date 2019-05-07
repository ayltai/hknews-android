package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.collection.SparseArrayCompat;
import androidx.core.util.Pair;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.SettingsActivity;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.AboutPresenter;
import com.github.ayltai.hknews.view.MainPresenter;
import com.github.ayltai.hknews.view.PagerPresenter;
import com.github.ayltai.hknews.view.Presenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.auto.value.AutoValue;
import com.mancj.materialsearchbar.MaterialSearchBar;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

import flow.ClassKey;

public final class MainView extends ScreenView implements MainPresenter.View, MaterialSearchBar.OnSearchActionListener, TextWatcher, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, Cacheable {
    @AutoValue
    public abstract static class Key extends ClassKey implements Parcelable {
        @Nonnull
        @NonNull
        static MainView.Key create() {
            return new AutoValue_MainView_Key();
        }
    }

    public static final MainView.Key KEY = MainView.Key.create();

    //region Subscriptions

    private final FlowableProcessor<Irrelevant>   settingsClicks         = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant>   newsClicks             = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant>   historiesClicks        = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant>   bookmarksClicks        = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant>   aboutClicks            = PublishProcessor.create();
    private final FlowableProcessor<List<String>> searchHistoriesChanges = PublishProcessor.create();

    //endregion

    //region Components

    private final SparseArrayCompat<Pair<Presenter, Presenter.View>> views = new SparseArrayCompat<>();

    private final MaterialSearchBar searchBar;
    private final ViewGroup         container;

    //endregion

    public MainView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_main, this, false);
        this.container = view.findViewById(R.id.content);

        this.searchBar = view.findViewById(R.id.searchBar);
        this.searchBar.setCardViewElevation(0);
        this.searchBar.inflateMenu(R.menu.top);
        this.searchBar.getMenu().setOnMenuItemClickListener(this);
        this.searchBar.setOnSearchActionListener(this);
        this.searchBar.addTextChangeListener(this);

        final BottomNavigationView navigationView = view.findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this);

        final PagerPresenter    pagerPresenter         = new PagerPresenter();
        final PagerView         pagerView              = new PagerView(context);
        final PagerPresenter    historyPagerPresenter  = new PagerPresenter();
        final HistoryPagerView  historyPagerView       = new HistoryPagerView(context);
        final PagerPresenter    bookmarkPagerPresenter = new PagerPresenter();
        final BookmarkPagerView bookmarkPagerView      = new BookmarkPagerView(context);
        final AboutPresenter    aboutPresenter         = new AboutPresenter();
        final AboutView         aboutView              = new AboutView(context);

        this.views.put(R.id.action_news, Pair.create(pagerPresenter, pagerView));
        this.views.put(R.id.action_histories, Pair.create(historyPagerPresenter, historyPagerView));
        this.views.put(R.id.action_bookmarks, Pair.create(bookmarkPagerPresenter, bookmarkPagerView));
        this.views.put(R.id.action_about, Pair.create(aboutPresenter, aboutView));

        this.addView(view);
        this.updateLayout(BaseView.LAYOUT_SCREEN);

        this.showView(R.id.action_news);
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> settingsClicks() {
        return this.settingsClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> newsClicks() {
        return this.newsClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> historiesClicks() {
        return this.historiesClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> bookmarksClicks() {
        return this.bookmarksClicks;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> aboutClicks() {
        return this.aboutClicks;
    }

    @Override
    public void showSettings() {
        final Activity activity = this.getActivity();
        if (activity == null) {
            SettingsActivity.show(this.getContext());
        } else {
            SettingsActivity.show(activity);
        }
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<List<String>> searchHistoriesChanges() {
        return this.searchHistoriesChanges;
    }

    @Override
    public void setSearchHistories(@Nonnull @NonNull @lombok.NonNull final List<String> searchHistories) {
        this.searchBar.setLastSuggestions(searchHistories);
    }

    @Override
    public void showNews() {
        this.showView(R.id.action_news);
    }

    @Override
    public void showHistories() {
        this.showView(R.id.action_histories);
    }

    @Override
    public void showBookmarks() {
        this.showView(R.id.action_bookmarks);
    }

    @Override
    public void showAbout() {
        this.showView(R.id.action_about);
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.container.getChildAt(0));
        pair.first.onViewAttached(pair.second);
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        this.searchHistoriesChanges.onNext(this.searchBar.getLastSuggestions());

        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.container.getChildAt(0));
        pair.first.onViewDetached();

        super.onDetachedFromWindow();
    }

    @Override
    public void onSearchStateChanged(final boolean enabled) {
        if (!enabled) this.onSearchConfirmed(null);
    }

    @Override
    public void onSearchConfirmed(@Nullable final CharSequence text) {
        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.container.getChildAt(0));
        if (pair.first instanceof PagerPresenter) ((PagerPresenter)pair.first).filter(text);
    }

    @Override
    public void onButtonClicked(final int buttonCode) {
        // Does nothing
    }

    @Override
    public void beforeTextChanged(@Nullable final CharSequence text, final int start, final int count, final int after) {
        // Does nothing
    }

    @Override
    public void onTextChanged(@Nullable final CharSequence text, final int start, final int before, final int count) {
        // Does nothing
    }

    @Override
    public void afterTextChanged(@Nullable final Editable editable) {
        this.onSearchConfirmed(editable);
    }

    @Override
    public boolean onMenuItemClick(@Nonnull @NonNull @lombok.NonNull final MenuItem item) {
        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.container.getChildAt(0));

        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (pair.second instanceof PagerView) {
                    ((PagerPresenter)pair.first).refresh();

                    return true;
                }

                return false;

            case R.id.action_clear:
                if (pair.second instanceof HistoryPagerView || pair.second instanceof BookmarkPagerView) {
                    ((PagerPresenter)pair.first).clear();

                    return true;
                }

                return false;

            case R.id.action_settings:
                SettingsActivity.show(this.getContext());

                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@Nonnull @NonNull @lombok.NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_news:
                this.newsClicks.onNext(Irrelevant.INSTANCE);
                return true;

            case R.id.action_histories:
                this.historiesClicks.onNext(Irrelevant.INSTANCE);
                return true;

            case R.id.action_bookmarks:
                this.bookmarksClicks.onNext(Irrelevant.INSTANCE);
                return true;

            case R.id.action_about:
                this.aboutClicks.onNext(Irrelevant.INSTANCE);
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onNavigationItemReselected(@Nonnull @NonNull @lombok.NonNull final MenuItem item) {
    }

    @Override
    public boolean handleBack() {
        if (this.searchBar.isSuggestionsVisible()) {
            this.searchBar.hideSuggestionsList();

            return true;
        }

        return false;
    }

    private void showView(@IdRes final int id) {
        final View view = this.container.getChildAt(0);

        final Pair<Presenter, Presenter.View> newPair = this.views.get(id);
        this.container.addView((View)newPair.second);
        newPair.first.onViewAttached(newPair.second);

        final Pair<Presenter, Presenter.View> oldPair = this.findPairByView(view);
        if (oldPair != null) {
            this.container.removeView(view);
            oldPair.first.onViewDetached();
        }

        switch (id) {
            case R.id.action_news:
                this.searchBar.getMenu().getMenu().findItem(R.id.action_refresh).setVisible(true);
                this.searchBar.getMenu().getMenu().findItem(R.id.action_clear).setVisible(false);

                this.searchBar.setVisibility(View.VISIBLE);

                break;

            case R.id.action_histories:
            case R.id.action_bookmarks:
                this.searchBar.getMenu().getMenu().findItem(R.id.action_refresh).setVisible(true);
                this.searchBar.getMenu().getMenu().findItem(R.id.action_clear).setVisible(true);

                this.searchBar.setVisibility(View.VISIBLE);

                break;

            case R.id.action_about:
                this.searchBar.setVisibility(View.GONE);

                break;

            default:
                break;
        }
    }

    @Nullable
    private Pair<Presenter, Presenter.View> findPairByView(@Nullable final View view) {
        if (view == null) return null;

        for (int i = 0; i < this.views.size(); i++) {
            final Pair<Presenter, Presenter.View> pair = this.views.valueAt(i);
            if (view == pair.second) return pair;
        }

        throw new IllegalArgumentException();
    }
}
