package com.github.ayltai.hknews.widget;

import java.util.Map;

import javax.annotation.Nonnull;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.PagerPresenter;

class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    //region Variables

    private final Map<Object, ListPresenter>  presenters = new ArrayMap<>();
    private final SparseArrayCompat<ListView> views      = new SparseArrayCompat<>();
    private final PagerPresenter              presenter;

    //endregion

    public PagerAdapter(@Nonnull @NonNull @lombok.NonNull final PagerPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getCount() {
        return this.presenter.getModel().size();
    }

    @Override
    public boolean isViewFromObject(@Nonnull @NonNull @lombok.NonNull final View view, @Nonnull @NonNull @lombok.NonNull final Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(final int position) {
        if (position < 0) return null;
        if (position < this.getCount()) return this.presenter.getModel().get(position);

        return null;
    }

    @Nonnull
    @NonNull
    @Override
    public Object instantiateItem(@Nonnull @NonNull @lombok.NonNull final ViewGroup container, final int position) {
        if (this.views.containsKey(position)) return this.views.get(position);

        final ListPresenter presenter = this.createListPresenter(this.presenter.getModel().get(position));
        final ListView      view      = Components.getInstance().getConfigComponent().userConfigurations().isCompactStyle() ? new CompactListView(container.getContext()) : new CozyListView(container.getContext());

        container.addView(view);

        presenter.onViewAttached(view);
        presenter.bindModel();

        this.presenters.put(view, presenter);
        this.views.put(position, view);

        return view;
    }

    @Override
    public void destroyItem(@Nonnull @NonNull @lombok.NonNull final ViewGroup container, final int position, @Nonnull @NonNull @lombok.NonNull final Object object) {
        this.presenters.remove(object).onViewDetached();
        this.views.remove(position);

        container.removeView((View)object);
    }

    public void refresh(final int position) {
        this.views.get(position).refreshes.onNext(Irrelevant.INSTANCE);
    }

    public void clear(final int position) {
        this.views.get(position).clears.onNext(Irrelevant.INSTANCE);
    }

    public void filter(@Nullable final CharSequence text) {
        for (int i = 0; i < this.views.size(); i++) this.presenters.get(this.views.valueAt(i)).filter(text);
    }

    @Nonnull
    @NonNull
    protected ListPresenter createListPresenter(@Nonnull @NonNull @lombok.NonNull final String category) {
        return new ListPresenter(category);
    }
}
