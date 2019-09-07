package com.github.ayltai.hknews.app;

import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.view.BookmarkListViewModel;
import com.github.ayltai.hknews.data.view.EmptyViewModel;
import com.github.ayltai.hknews.data.view.HistoryListViewModel;
import com.github.ayltai.hknews.data.view.ListViewModel;
import com.github.ayltai.hknews.data.view.LocalListViewModel;
import com.github.ayltai.hknews.databinding.FragmentListSearchBinding;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

public abstract class SearchFragment<C extends ViewDataBinding> extends LocalListFragment<FragmentListSearchBinding, C> {
    public static final class Cozy extends SearchFragment<ViewItemCozyBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CozyListAdapter(items);
        }
    }

    public static final class Compact extends SearchFragment<ViewItemCompactBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CompactListAdapter(items);
        }
    }

    private int searchType;

    @CallSuper
    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);

        final Activity activity = this.getActivity();
        if (activity != null) searchView.setOnCloseListener(() -> Navigation.findNavController(activity, R.id.navHostFragment).navigateUp());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(@Nullable final String query) {
                SearchFragment.this.onRefresh();

                return true;
            }

            @Override
            public boolean onQueryTextChange(@Nullable final String newText) {
                return this.onQueryTextSubmit(searchView.getQuery().toString());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@Nonnull @NonNull @lombok.NonNull final View view, @Nullable final Bundle savedInstanceState) {
        if (this.getArguments() != null) this.searchType = this instanceof SearchFragment.Compact ? SearchFragment$CompactArgs.fromBundle(this.getArguments()).getSearchType() : SearchFragment$CozyArgs.fromBundle(this.getArguments()).getSearchType();
    }

    @Override
    @LayoutRes
    protected int getLayoutId() {
        return R.layout.fragment_list_search;
    }

    @Override
    @MenuRes
    protected int getMenu() {
        return 0;
    }

    @Override
    protected void setUpEmptyView() {
        final EmptyViewModel model = new EmptyViewModel();
        model.setIcon(R.drawable.ic_search_white_24dp);
        model.setTitle(R.string.empty_search_title);
        model.setDescription(R.string.empty_search_description);

        this.binding.setModel(model);
    }

    @Nonnull
    @NonNull
    protected ListViewModel getListViewModel() {
        if (this.model == null) {
            final Activity activity = ViewUtils.getActivity(this.getContext());
            if (activity == null) throw new IllegalStateException("Unable to get Application reference");

            Class<? extends ListViewModel> clazz = null;
            switch (this.searchType) {
                case Constants.SEARCH_TYPE_NEWS:
                    clazz = LocalListViewModel.class;
                    break;

                case Constants.SEARCH_TYPE_BOOKMARKS:
                    clazz = BookmarkListViewModel.class;
                    break;

                case Constants.SEARCH_TYPE_HISTORIES:
                    clazz = HistoryListViewModel.class;
                    break;

                case Constants.SEARCH_TYPE_UNDEFINED:
                default:
                    throw new IllegalArgumentException("Unrecognized search type");
            }

            if (clazz != null) this.model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication())
                .create(clazz);
        }

        this.model.setKeywords(this.binding.searchView.getQuery().toString());

        return this.model;
    }

    @Override
    protected int getSearchType() {
        return Constants.SEARCH_TYPE_UNDEFINED;
    }

    @CallSuper
    @Override
    protected void init() {
        super.init();

        final Activity activity = this.getActivity();
        if (activity != null) this.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigateUp());
    }

    @CallSuper
    @Override
    protected void onRefresh() {
        this.category = null;

        super.onRefresh();
    }

    @Override
    protected void updateSubheader() {
        //
    }
}
