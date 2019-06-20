package com.github.ayltai.hknews.widget;

import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.SparseArrayCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Pair;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.SettingsActivity;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.util.DateUtils;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.AboutPresenter;
import com.github.ayltai.hknews.view.BookmarkListPresenter;
import com.github.ayltai.hknews.view.CategoryPresenter;
import com.github.ayltai.hknews.view.HistoryListPresenter;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.MainPresenter;
import com.github.ayltai.hknews.view.Presenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.auto.value.AutoValue;
import com.mancj.materialsearchbar.MaterialSearchBar;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

import flow.ClassKey;

public final class MainView extends ScreenView implements MainPresenter.View, MaterialSearchBar.OnSearchActionListener, TextWatcher, Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, Cacheable {
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

    private final FlowableProcessor<Irrelevant> settingsClicks  = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant> newsClicks      = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant> historiesClicks = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant> bookmarksClicks = PublishProcessor.create();
    private final FlowableProcessor<Irrelevant> aboutClicks     = PublishProcessor.create();

    //endregion

    //region Components

    private final SparseArrayCompat<Pair<Presenter, Presenter.View>> views = new SparseArrayCompat<>();

    private final Toolbar           toolbar;
    private final MaterialSearchBar searchBar;
    private final TextView          subheader;
    private final ViewGroup         content;
    private final BackdropBehavior  behavior;

    private final CategoryPresenter categoryPresenter;
    private final CategoryView      categoryView;

    //endregion

    private Timer timer;

    public MainView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_main, this, false);

        this.content  = view.findViewById(R.id.content);
        this.behavior = (BackdropBehavior)((CoordinatorLayout.LayoutParams)view.findViewById(R.id.container).getLayoutParams()).getBehavior();

        this.toolbar = view.findViewById(R.id.toolbar);
        this.toolbar.setOnMenuItemClickListener(this);

        this.searchBar = view.findViewById(R.id.searchBar);
        this.searchBar.setCardViewElevation(0);
        this.searchBar.setOnSearchActionListener(this);
        this.searchBar.addTextChangeListener(this);

        final BottomNavigationView navigationView = view.findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this);

        this.categoryPresenter = new CategoryPresenter();
        this.categoryView      = new CategoryView(context);

        ((ViewGroup)view.findViewById(R.id.appBarLayout)).addView(this.categoryView);

        this.subheader = view.findViewById(R.id.subheader);
        this.updateSubheader(context, this.categoryPresenter.getCategoryName());

        this.views.put(R.id.action_news, Pair.create(new ListPresenter(), this.createListView(new EmptyState())));
        this.views.put(R.id.action_histories, Pair.create(new HistoryListPresenter(), this.createListView(new HistoryEmptyState())));
        this.views.put(R.id.action_bookmarks, Pair.create(new BookmarkListPresenter(), this.createListView(new BookmarkEmptyState())));
        this.views.put(R.id.action_about, Pair.create(new AboutPresenter(), new AboutView(context)));

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
        this.manageDisposable(this.categoryView.attaches().subscribe(irrelevant -> this.categoryPresenter.onViewAttached(this.categoryView)));
        this.manageDisposable(this.categoryView.detaches().subscribe(irrelevant -> this.categoryPresenter.onViewDetached()));

        this.manageDisposable(this.categoryView.selects().subscribe(categoryName -> {
            this.updateSubheader(this.getContext(), categoryName);

            this.behavior.collapse();

            final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.content.getChildAt(0));
            if (pair != null && pair.first != null && pair.second != null) {
                if (pair.first instanceof ListPresenter) {
                    final ListPresenter presenter = (ListPresenter)pair.first;

                    presenter.setCategoryName(categoryName);
                    presenter.bindModel();
                }

                pair.first.onViewAttached(pair.second);
            }
        }));

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainView.this.updateSubheader(MainView.this.getContext(), MainView.this.categoryPresenter.getCategoryName());
            }
        }, 0, Constants.LAST_UPDATED_TIME);

        super.onAttachedToWindow();
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        this.timer.cancel();
        this.timer.purge();

        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.content.getChildAt(0));
        if (pair != null && pair.first != null) pair.first.onViewDetached();

        super.onDetachedFromWindow();
    }

    @Override
    public void onSearchStateChanged(final boolean enabled) {
        this.searchBar.clearSuggestions();

        if (!enabled) {
            this.toolbar.setVisibility(View.VISIBLE);
            this.onSearchConfirmed(null);

            this.searchBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchConfirmed(@Nullable final CharSequence text) {
        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.content.getChildAt(0));
        if (pair != null && pair.first instanceof ListPresenter) ((ListPresenter)pair.first).filter(text);
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
        this.onSearchConfirmed(text);
    }

    @Override
    public void afterTextChanged(@Nullable final Editable editable) {
        // Does nothing
    }

    @Override
    public boolean onMenuItemClick(@Nonnull @NonNull @lombok.NonNull final MenuItem item) {
        final Pair<Presenter, Presenter.View> pair = this.findPairByView(this.content.getChildAt(0));

        switch (item.getItemId()) {
            case R.id.action_search:
                this.toolbar.setVisibility(View.GONE);
                this.searchBar.setVisibility(View.VISIBLE);
                this.searchBar.enableSearch();

                return true;

            case R.id.action_refresh:
                if (pair == null) return false;

                if (pair.second instanceof ListView) {
                    if (pair.first != null) ((ListView)pair.second).refreshes.onNext(Irrelevant.INSTANCE);

                    return true;
                }

                return false;

            case R.id.action_clear:
                if (pair == null) return false;

                if (pair.second instanceof ListView) {
                    if (pair.first != null) ((ListView)pair.second).clears.onNext(Irrelevant.INSTANCE);

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

    private ListView createListView(@Nonnull @NonNull @lombok.NonNull final EmptyState emptyState) {
        return Components.getInstance().getConfigComponent().userConfigurations().isCompactStyle() ? new CompactListView(content.getContext(), emptyState) : new CozyListView(content.getContext(), emptyState);
    }

    private void showView(@IdRes final int id) {
        final View view = this.content.getChildAt(0);

        final Pair<Presenter, Presenter.View> newPair = this.views.get(id);
        if (newPair != null && newPair.first != null && newPair.second != null) {
            if (id == R.id.action_about) {
                this.subheader.setVisibility(View.GONE);
            } else {
                this.subheader.setVisibility(View.VISIBLE);

                ((ListPresenter)newPair.first).setCategoryName(this.categoryPresenter.getCategoryName());
            }

            this.content.addView((View)newPair.second);
            newPair.first.onViewAttached(newPair.second);
        }

        final Pair<Presenter, Presenter.View> oldPair = this.findPairByView(view);
        if (oldPair != null && oldPair.first != null) {
            this.content.removeView(view);
            oldPair.first.onViewDetached();
        }

        if (id == R.id.action_about) {
            this.toolbar.getMenu().findItem(R.id.action_search).setVisible(false);
            this.toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);

            this.toolbar.setNavigationIcon(null);
        } else {
            this.toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
            this.toolbar.getMenu().findItem(R.id.action_refresh).setVisible(true);

            this.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    private void updateSubheader(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final String categoryName) {
        this.subheader.setText(String.format(context.getResources().getString(R.string.last_updated), categoryName, DateUtils.getHumanReadableDate(context, Components.getInstance().getConfigComponent().userConfigurations().getLastUpdatedDate(categoryName))));
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
