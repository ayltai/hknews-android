package com.github.ayltai.hknews.app;

import javax.annotation.Nonnull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private static final String ARG_IS_INITIALIZED = "IS_INITIALIZED";

    protected View    view;
    protected Toolbar toolbar;
    protected boolean isInitialized;

    @CallSuper
    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        if (this.view == null) {
            this.view    = LayoutInflater.from(this.getContext()).inflate(this.getLayoutId(), container, false);
            this.toolbar = this.view.findViewById(this.getToolbarId());
        }

        return this.view;
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.restoreViewState(savedInstanceState);

        if (!this.isInitialized) {
            this.isInitialized = true;

            this.init();
        }
    }

    @CallSuper
    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        this.restoreViewState(savedInstanceState);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@Nonnull @NonNull @lombok.NonNull final Bundle outState) {
        outState.putBoolean(BaseFragment.ARG_IS_INITIALIZED, this.isInitialized);

        super.onSaveInstanceState(outState);
    }

    @CallSuper
    protected void restoreViewState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BaseFragment.ARG_IS_INITIALIZED)) this.isInitialized = savedInstanceState.getBoolean(BaseFragment.ARG_IS_INITIALIZED, false);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @IdRes
    protected abstract int getToolbarId();

    @CallSuper
    protected void init() {
        this.setUpMenuItems();
    }

    protected void setUpMenuItems() {
    }
}
