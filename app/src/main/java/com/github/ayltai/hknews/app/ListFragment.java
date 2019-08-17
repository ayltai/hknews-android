package com.github.ayltai.hknews.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.view.EmptyViewModel;
import com.github.ayltai.hknews.data.view.ListViewModel;
import com.github.ayltai.hknews.databinding.FragmentListBinding;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.widget.BackdropBehavior;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

import io.reactivex.disposables.Disposable;
import io.supercharge.shimmerlayout.ShimmerLayout;

public abstract class ListFragment<B extends ViewDataBinding> extends BaseFragment {
    public static final class Cozy extends ListFragment<ViewItemCozyBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            if (this.adapter == null) this.adapter = new CozyListAdapter(items);
            return this.adapter;
        }
    }

    public static final class Compact extends ListFragment<ViewItemCompactBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            if (this.adapter == null) this.adapter = new CompactListAdapter(items);
            return this.adapter;
        }
    }

    //region Variables

    protected SwipeRefreshLayout  swipeRefreshLayout;
    protected FragmentListBinding binding;
    protected ListViewModel       model;
    protected String              category;

    ListAdapter<B> adapter;

    private BackdropBehavior behavior;
    private RecyclerView     recyclerView;
    private ShimmerLayout    shimmerLayout;
    private View             emptyView;
    private Disposable       disposable;

    //endregion

    @CallSuper
    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final Bundle bundle = this.getArguments();
        this.category = bundle == null || !bundle.containsKey(Constants.ARG_CATEGORY) ? this.getResources().getStringArray(R.array.category_values)[0] : bundle.getString(Constants.ARG_CATEGORY);

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        if (this.behavior == null) this.behavior = (BackdropBehavior)((CoordinatorLayout.LayoutParams)view.findViewById(R.id.cardView).getLayoutParams()).getBehavior();

        if (this.recyclerView == null) {
            this.recyclerView = view.findViewById(this.getRecyclerViewId());
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        if (this.swipeRefreshLayout == null) {
            this.swipeRefreshLayout = view.findViewById(this.getSwipeRefreshLayoutId());
            this.swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        }

        if (this.emptyView == null) this.emptyView = view.findViewById(android.R.id.empty);

        return view;
    }

    @CallSuper
    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.ARG_CATEGORY)) this.category = savedInstanceState.getString(Constants.ARG_CATEGORY);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putString(Constants.ARG_CATEGORY, this.category);

        super.onSaveInstanceState(outState);
    }

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @IdRes
    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @IdRes
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipeRefreshLayout;
    }

    @IdRes
    protected int getRecyclerViewId() {
        return R.id.recyclerView;
    }

    @Nonnull
    @NonNull
    protected abstract ListAdapter<B> getListAdapter(@Nonnull @NonNull List<Item> items);

    @Nonnull
    @NonNull
    protected ListViewModel getListViewModel() {
        if (this.model == null) {
            final Activity activity = ViewUtils.getActivity(this.getContext());
            if (activity == null) throw new IllegalStateException("Unable to get Application reference");

            this.model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication())
                .create(ListViewModel.class);
        }

        return this.model;
    }

    @CallSuper
    @Override
    protected void init() {
        super.init();

        this.binding = DataBindingUtil.bind(this.view);

        this.behavior.collapse();

        this.setUpCategoryView();
        this.setUpEmptyView();
        this.setUpPlaceholder(Components.getInstance().getConfigComponent().userConfigurations().isCompactStyle());
        this.onRefresh();
    }

    @CallSuper
    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

        this.toolbar.inflateMenu(R.menu.news);
    }

    @Override
    protected void setUpMenuItems() {
        final Menu     menu             = this.toolbar.getMenu();
        final MenuItem refreshMenuItem  = menu.findItem(R.id.action_refresh);
        final MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);

        if (refreshMenuItem != null) refreshMenuItem.setOnMenuItemClickListener(item -> {
            this.onRefresh();

            return true;
        });

        if (settingsMenuItem != null) settingsMenuItem.setOnMenuItemClickListener(item -> {
            if (this.getContext() != null) SettingsActivity.show(this.getContext());

            return true;
        });
    }

    protected void setUpEmptyView() {
        final EmptyViewModel model = new ViewModelProvider.NewInstanceFactory().create(EmptyViewModel.class);
        model.setIcon(R.drawable.ic_empty_news_black_24dp);
        model.setTitle(R.string.empty_news_title);
        model.setDescription(R.string.empty_news_description);
        model.setAction(R.string.empty_news_action);

        this.binding.setModel(model);

        this.binding.emptyAction.setOnClickListener(view -> {
            if (this.getContext() != null) SettingsActivity.show(this.getContext());
        });
    }

    private void setUpPlaceholder(final boolean isCompactStyle) {
        final ViewStub viewStub = this.binding.container.findViewById(R.id.placeholder);
        viewStub.setLayoutResource(isCompactStyle ? R.layout.view_list_compact_placeholder : R.layout.view_list_cozy_placeholder);

        this.shimmerLayout = viewStub.inflate().findViewById(R.id.shimmerLayout);
        this.shimmerLayout.startShimmerAnimation();
    }

    private void setUpCategoryView() {
        final List<String> categories = Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getCategoryNames();

        final Map<Integer, String> actions = new ArrayMap<>();
        actions.put(R.id.action_category_hongkong, categories.get(0));
        actions.put(R.id.action_category_international, categories.get(1));
        actions.put(R.id.action_category_china, categories.get(2));
        actions.put(R.id.action_category_economy, categories.get(3));
        actions.put(R.id.action_category_property, categories.get(4));
        actions.put(R.id.action_category_entertainment, categories.get(5));
        actions.put(R.id.action_category_sports, categories.get(6));
        actions.put(R.id.action_category_supplement, categories.get(7));
        actions.put(R.id.action_category_education, categories.get(8));

        final Menu menu = this.binding.categoryView.getMenu();
        for (final Map.Entry<Integer, String> action : actions.entrySet()) menu.findItem(action.getKey()).setVisible(categories.contains(action.getValue()));

        this.binding.categoryView.setNavigationItemSelectedListener(item -> {
            this.behavior.collapse();

            this.category = actions.get(item.getItemId());

            this.onRefresh();

            return true;
        });
    }

    private void onRefresh() {
        this.binding.subheader.setText(this.category);

        this.swipeRefreshLayout.setRefreshing(true);

        if (this.disposable != null) this.disposable.dispose();

        this.disposable = this.getListViewModel()
            .getItems(this.category)
            .compose(RxUtils.applySingleBackgroundToMainSchedulers())
            .subscribe(
                items -> {
                    this.recyclerView.setAdapter(this.getListAdapter(items));
                    this.swipeRefreshLayout.setRefreshing(false);

                    if (items.isEmpty()) {
                        this.recyclerView.setVisibility(View.GONE);
                        this.emptyView.setVisibility(View.VISIBLE);
                    } else {
                        this.recyclerView.setVisibility(View.VISIBLE);
                        this.emptyView.setVisibility(View.GONE);
                    }

                    this.shimmerLayout.stopShimmerAnimation();
                    this.shimmerLayout.setVisibility(View.GONE);
                },
                RxUtils::handleError
            );
    }
}
