package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.view.ItemPresenter;
import com.github.ayltai.hknews.view.ListPresenter;

public final class ListAdapter extends RecyclerView.Adapter<ItemViewHolder<ItemPresenter<ItemPresenter.View>, ItemView>> {
    private final ListPresenter presenter;
    private final boolean       isCompactStyle;

    public ListAdapter(@Nonnull @NonNull @lombok.NonNull final ListPresenter presenter, final boolean isCompactStyle) {
        this.presenter      = presenter;
        this.isCompactStyle = isCompactStyle;
    }

    @Override
    public int getItemCount() {
        return this.presenter.getModel().size();
    }

    @Nonnull
    @NonNull
    @Override
    public ItemViewHolder<ItemPresenter<ItemPresenter.View>, ItemView> onCreateViewHolder(@Nonnull @NonNull @lombok.NonNull final ViewGroup parent, final int viewType) {
        if (this.isCompactStyle) return new ItemViewHolder<>(new ItemPresenter<>(), new CompactItemView(parent.getContext()));

        return new ItemViewHolder<>(new ItemPresenter<>(), new CozyItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<ItemPresenter<ItemPresenter.View>, ItemView> holder, final int position) {
        holder.presenter.onViewDetached();
        holder.presenter.setModel(this.presenter.getModel().get(position));
        holder.presenter.onViewAttached(holder.view);
        holder.presenter.bindModel();
    }
}
