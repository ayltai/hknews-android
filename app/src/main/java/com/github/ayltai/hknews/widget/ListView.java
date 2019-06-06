package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.MultiStatePresenter;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public abstract class ListView extends BaseView implements ListPresenter.View, Cacheable {
    //region Subscriptions

    protected final FlowableProcessor<Irrelevant> refreshes = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> clears    = PublishProcessor.create();

    //endregion

    //region Components

    protected ListAdapter         adapter;
    protected RecyclerView        recyclerView;
    protected SwipeRefreshLayout  swipeRefreshLayout;
    protected MultiStatePresenter multiStatePresenter;
    protected MultiStateView      multiStateView;

    //endregion

    protected EmptyState emptyState;

    protected ListView(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final EmptyState emptyState) {
        super(context);

        this.emptyState = emptyState;

        final View view = LayoutInflater.from(context).inflate(this.getLayoutId(), this, false);

        this.multiStatePresenter = new MultiStatePresenter();
        this.multiStateView      = new MultiStateView(context);

        this.multiStatePresenter.setEmptyIcon(this.getEmptyIcon());
        this.multiStatePresenter.setEmptyTitle(this.getEmptyTitle());
        this.multiStatePresenter.setEmptyDescription(this.getEmptyDescription());
        this.multiStatePresenter.setEmptyAction(this.getEmptyAction());
        this.multiStatePresenter.setEmptyClickListener(this.getEmptyClickListener());

        ((ViewGroup)view.findViewById(R.id.container)).addView(this.multiStateView);

        this.recyclerView = view.findViewById(this.getRecyclerViewId());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.swipeRefreshLayout = view.findViewById(this.getSwipeRefreshLayoutId());
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            this.swipeRefreshLayout.setRefreshing(true);

            if (this.recyclerView.getAdapter() != null && this.recyclerView.getAdapter().getItemCount() > 0) this.recyclerView.scrollToPosition(0);

            this.refreshes.onNext(Irrelevant.INSTANCE);
        });

        this.addView(view);
        this.updateLayout(BaseView.LAYOUT_SCREEN);
    }

    //region Properties

    protected abstract boolean isCompactStyle();

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.view_list;
    }

    @IdRes
    protected int getRecyclerViewId() {
        return R.id.recyclerView;
    }

    @IdRes
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipeRefreshLayout;
    }

    @DrawableRes
    protected int getEmptyIcon() {
        return this.emptyState.getIcon();
    }

    @StringRes
    protected int getEmptyTitle() {
        return this.emptyState.getTitle();
    }

    @StringRes
    protected int getEmptyDescription() {
        return this.emptyState.getDescription();
    }

    @StringRes
    protected int getEmptyAction() {
        return this.emptyState.getAction();
    }

    @Nonnull
    @NonNull
    protected View.OnClickListener getEmptyClickListener() {
        return this.emptyState.getClickListener(this);
    }

    //endregion

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> refreshes() {
        return this.refreshes;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> clears() {
        return this.clears;
    }

    @Override
    public void update() {
        this.hideLoadingView();

        this.recyclerView.setAdapter(this.adapter = new ListAdapter((ListPresenter)this.presenter, this.isCompactStyle()));
    }

    @Override
    public void clear() {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {
        this.multiStatePresenter.setState(MultiStatePresenter.State.EMPTY);
        this.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        this.multiStatePresenter.setState(MultiStatePresenter.State.HIDDEN);
        this.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingView() {
        this.swipeRefreshLayout.setRefreshing(true);

        if (this.adapter == null || this.adapter.getItemCount() == 0) {
            this.multiStatePresenter.setState(MultiStatePresenter.State.LOADING);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoadingView() {
        this.swipeRefreshLayout.setRefreshing(false);
        this.multiStatePresenter.setState(MultiStatePresenter.State.HIDDEN);
        this.recyclerView.setVisibility(View.VISIBLE);
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.manageDisposable(this.multiStateView.attaches().subscribe(irrelevant -> this.multiStatePresenter.onViewAttached(this.multiStateView)));
        this.manageDisposable(this.multiStateView.detaches().subscribe(irrelevant -> this.multiStatePresenter.onViewDetached()));
    }
}
