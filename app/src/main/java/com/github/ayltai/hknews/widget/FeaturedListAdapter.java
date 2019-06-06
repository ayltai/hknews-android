package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.view.FeaturedItemPresenter;

public final class FeaturedListAdapter extends RecyclerView.Adapter<ItemViewHolder<FeaturedItemPresenter<FeaturedItemPresenter.View>, FeaturedItemView>> {
    private final List<Item> items;

    public FeaturedListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Nonnull
    @NonNull
    @Override
    public ItemViewHolder<FeaturedItemPresenter<FeaturedItemPresenter.View>, FeaturedItemView> onCreateViewHolder(@Nonnull @NonNull @lombok.NonNull final ViewGroup parent, final int viewType) {
        return new ItemViewHolder<>(new FeaturedItemPresenter<>(), new FeaturedItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<FeaturedItemPresenter<FeaturedItemPresenter.View>, FeaturedItemView> holder, final int position) {
        holder.presenter.onViewDetached();
        holder.presenter.setModel(this.items.get(position));
        holder.presenter.onViewAttached(holder.view);
        holder.presenter.bindModel();
    }
}
