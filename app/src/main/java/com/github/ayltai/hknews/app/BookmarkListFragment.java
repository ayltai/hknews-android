package com.github.ayltai.hknews.app;

import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.view.BookmarkListViewModel;
import com.github.ayltai.hknews.data.view.EmptyViewModel;
import com.github.ayltai.hknews.data.view.ListViewModel;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

public abstract class BookmarkListFragment<B extends ViewDataBinding> extends ListFragment<B> {
    public static final class Cozy extends BookmarkListFragment<ViewItemCozyBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            if (this.adapter == null) this.adapter = new CozyListAdapter(items);
            return this.adapter;
        }
    }

    public static final class Compact extends BookmarkListFragment<ViewItemCompactBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            if (this.adapter == null) this.adapter = new CompactListAdapter(items);
            return this.adapter;
        }
    }

    @CallSuper
    @Override
    protected void init() {
        super.init();

        this.swipeRefreshLayout.setEnabled(false);
    }

    @CallSuper
    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

        this.toolbar.inflateMenu(R.menu.bookmark);
    }

    @Override
    protected void setUpEmptyView() {
        final EmptyViewModel model = new EmptyViewModel();
        model.setIcon(R.drawable.ic_empty_news_black_24dp);
        model.setTitle(R.string.empty_bookmark_title);
        model.setDescription(R.string.empty_bookmark_description);
        model.setAction(R.string.empty_bookmark_action);

        this.binding.setModel(model);

        this.binding.emptyAction.setOnClickListener(view -> {
            if (this.getContext() != null) SettingsActivity.show(this.getContext());
        });
    }

    @Nonnull
    @NonNull
    protected ListViewModel getListViewModel() {
        if (this.model == null) {
            final Activity activity = ViewUtils.getActivity(this.getContext());
            if (activity == null) throw new IllegalStateException("Unable to get Application reference");

            this.model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication())
                .create(BookmarkListViewModel.class);
        }

        return this.model;
    }
}
