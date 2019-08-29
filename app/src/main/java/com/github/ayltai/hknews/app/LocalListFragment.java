package com.github.ayltai.hknews.app;

import androidx.annotation.CallSuper;
import androidx.databinding.ViewDataBinding;

public abstract class LocalListFragment<P extends ViewDataBinding, C extends ViewDataBinding> extends ListFragment<P, C> {
    @CallSuper
    @Override
    protected void init() {
        super.init();

        this.swipeRefreshLayout.setEnabled(false);
    }
}
