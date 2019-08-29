package com.github.ayltai.hknews.app;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.collection.ArrayMap;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.disposables.CompositeDisposable;
import io.supercharge.shimmerlayout.ShimmerLayout;

import com.github.ayltai.hknews.BR;
import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.view.EmptyViewModel;
import com.github.ayltai.hknews.data.view.ListViewModel;
import com.github.ayltai.hknews.databinding.FragmentListBinding;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.DateUtils;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.widget.BackdropBehavior;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;
import com.google.android.material.navigation.NavigationView;

public abstract class ListFragment<P extends ViewDataBinding, C extends ViewDataBinding> extends BaseFragment {
    public static final class Cozy extends ListFragment<FragmentListBinding, ViewItemCozyBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CozyListAdapter(items);
        }
    }

    public static final class Compact extends ListFragment<FragmentListBinding, ViewItemCompactBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CompactListAdapter(items);
        }
    }

    //region Constants

    protected static final String ARG_CATEGORY = "CATEGORY";
    protected static final String ARG_POSITION = "POSITION";

    //endregion

    //region Variables

    protected P                  binding;
    protected ListViewModel      model;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected String             category;

    private BackdropBehavior    behavior;
    private RecyclerView        recyclerView;
    private ShimmerLayout       shimmerLayout;
    private View                emptyView;
    private CompositeDisposable disposables;
    private Timer               timer;
    private int                 position;

    //endregion

    @CallSuper
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.disposables = new CompositeDisposable();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.disposables.dispose();
    }

    @CallSuper
    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final Bundle bundle = this.getArguments();
        this.category = bundle == null || !bundle.containsKey(ListFragment.ARG_CATEGORY) ? this.getResources().getStringArray(R.array.category_values)[0] : bundle.getString(ListFragment.ARG_CATEGORY);
        this.position = bundle == null || !bundle.containsKey(ListFragment.ARG_POSITION) ? Components.getInstance().getConfigComponent().userConfigurations().getPosition(this.getClass().getSimpleName(), this.category) : bundle.getInt(ListFragment.ARG_POSITION);

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final CardView cardView = view.findViewById(R.id.cardView);
        if (cardView != null) this.behavior = (BackdropBehavior)((CoordinatorLayout.LayoutParams)cardView.getLayoutParams()).getBehavior();

        this.recyclerView = view.findViewById(this.getRecyclerViewId());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        this.swipeRefreshLayout = view.findViewById(this.getSwipeRefreshLayoutId());
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            this.resetPosition();
            this.onRefresh();
        });

        this.emptyView = view.findViewById(android.R.id.empty);

        return view;
    }

    @CallSuper
    @Override
    public void onAttach(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super.onAttach(context);

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(ListFragment.this::updateSubheader);
            }
        }, 0, Constants.LAST_UPDATED_TIME);
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();

        this.timer.cancel();
        this.timer.purge();

        Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .setPosition(this.getClass().getSimpleName(), this.category, ((LinearLayoutManager)this.recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putString(ListFragment.ARG_CATEGORY, this.category);
        outState.putInt(ListFragment.ARG_POSITION, this.position);

        super.onSaveInstanceState(outState);
    }

    @CallSuper
    @Override
    protected void restoreViewState(@Nullable final Bundle savedInstanceState) {
        super.restoreViewState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ListFragment.ARG_CATEGORY)) this.category = savedInstanceState.getString(ListFragment.ARG_CATEGORY);
            if (savedInstanceState.containsKey(ListFragment.ARG_POSITION)) this.position = savedInstanceState.getInt(ListFragment.ARG_POSITION);
        }
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

    @MenuRes
    protected int getMenu() {
        return R.menu.news;
    }

    @Nonnull
    @NonNull
    protected abstract ListAdapter<C> getListAdapter(@Nonnull @NonNull List<Item> items);

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

    protected int getSearchType() {
        return Constants.SEARCH_TYPE_NEWS;
    }

    @CallSuper
    @Override
    protected void init() {
        super.init();

        this.binding = DataBindingUtil.bind(this.view);

        if (this.behavior != null) this.behavior.collapse();

        this.setUpCategoryView();
        this.setUpEmptyView();
        this.setUpPlaceholder(Components.getInstance().getConfigComponent().userConfigurations().isCompactStyle());
        this.onRefresh();
    }

    @CallSuper
    @Override
    protected void setUpMenuItems() {
        if (this.getMenu() == 0) return;

        this.toolbar.inflateMenu(this.getMenu());

        final Menu     menu             = this.toolbar.getMenu();
        final MenuItem searchMenuItem   = menu.findItem(R.id.action_search);
        final MenuItem refreshMenuItem  = menu.findItem(R.id.action_refresh);
        final MenuItem clearMenuItem    = menu.findItem(R.id.action_clear);
        final MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);

        if (searchMenuItem != null) searchMenuItem.setOnMenuItemClickListener(item -> {
            final Activity activity = ViewUtils.getActivity(this.getContext());
            if (activity != null) Navigation.findNavController(activity, R.id.navHostFragment).navigate(ListFragment$CompactDirections.searchAction(this.getSearchType()));

            return true;
        });

        if (refreshMenuItem != null) refreshMenuItem.setOnMenuItemClickListener(item -> {
            this.resetPosition();
            this.onRefresh();

            return true;
        });

        if (clearMenuItem != null) clearMenuItem.setOnMenuItemClickListener(item -> {
            this.onClear();

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

        this.binding.setVariable(BR.model, model);

        this.binding.getRoot().findViewById(R.id.empty_action).setOnClickListener(view -> {
            if (this.getContext() != null) SettingsActivity.show(this.getContext());
        });
    }

    private void setUpPlaceholder(final boolean isCompactStyle) {
        final ViewStub viewStub = this.binding.getRoot().findViewById(R.id.container).findViewById(R.id.placeholder);
        viewStub.setLayoutResource(isCompactStyle ? R.layout.view_list_compact_placeholder : R.layout.view_list_cozy_placeholder);

        this.shimmerLayout = viewStub.inflate().findViewById(R.id.shimmerLayout);
        this.shimmerLayout.startShimmerAnimation();
    }

    private void setUpCategoryView() {
        final List<String> categories = Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .getCategoryNames();

        int i = 0;

        final Map<Integer, String> actions = new ArrayMap<>();
        actions.put(R.id.action_category_hongkong, categories.get(i++));
        actions.put(R.id.action_category_international, categories.get(i++));
        actions.put(R.id.action_category_china, categories.get(i++));
        actions.put(R.id.action_category_economy, categories.get(i++));
        actions.put(R.id.action_category_property, categories.get(i++));
        actions.put(R.id.action_category_entertainment, categories.get(i++));
        actions.put(R.id.action_category_sports, categories.get(i++));
        actions.put(R.id.action_category_supplement, categories.get(i++));
        actions.put(R.id.action_category_education, categories.get(i));

        final NavigationView categoryView = this.binding.getRoot().findViewById(R.id.category_view);
        if (categoryView != null) {
            final Menu menu = categoryView.getMenu();
            for (final Map.Entry<Integer, String> action : actions.entrySet()) menu.findItem(action.getKey()).setVisible(categories.contains(action.getValue()));

            categoryView.setNavigationItemSelectedListener(item -> {
                this.behavior.collapse();

                this.category = actions.get(item.getItemId());

                this.resetPosition();
                this.onRefresh();

                return true;
            });
        }
    }

    @CallSuper
    protected void onRefresh() {
        this.swipeRefreshLayout.setRefreshing(true);
        this.recyclerView.setVisibility(View.GONE);
        this.emptyView.setVisibility(View.GONE);
        this.shimmerLayout.startShimmerAnimation();
        this.shimmerLayout.setVisibility(View.VISIBLE);

        this.disposables.add(this.getListViewModel()
            .getItems(this.category)
            .compose(RxUtils.applySingleBackgroundToMainSchedulers())
            .subscribe(
                items -> {
                    this.recyclerView.setAdapter(this.getListAdapter(items));
                    if (this.position > 0) this.recyclerView.scrollToPosition(this.position);

                    this.swipeRefreshLayout.setRefreshing(false);

                    if (items.isEmpty()) {
                        this.recyclerView.setVisibility(View.GONE);
                        this.emptyView.setVisibility(View.VISIBLE);
                    } else {
                        this.recyclerView.setVisibility(View.VISIBLE);
                        this.emptyView.setVisibility(View.GONE);
                    }

                    this.recyclerView.setVisibility(View.VISIBLE);
                    this.shimmerLayout.stopShimmerAnimation();
                    this.shimmerLayout.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).post(this::updateSubheader);
                },
                RxUtils::handleError
            ));
    }

    protected void onClear() {
        this.disposables.add(this.getListViewModel()
            .clear(Components.getInstance().getConfigComponent().userConfigurations().getSourceNames(), this.category)
            .subscribe(
                irrelevant -> { },
                RxUtils::handleError
            ));
    }

    protected void resetPosition() {
        this.position = 0;

        Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .setPosition(this.getClass().getSimpleName(), this.category, 0);
    }

    protected void updateSubheader() {
        if (this.binding != null) ((TextView)this.binding.getRoot().findViewById(R.id.subheader)).setText(this.getContext() == null ? this.category : this.getString(R.string.last_updated, this.category, DateUtils.getHumanReadableDate(this.getContext(), Components.getInstance().getConfigComponent().userConfigurations().getLastUpdatedDate(this.category))));
    }
}
