package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.view.ItemPresenter;
import com.github.ayltai.hknews.view.ListPresenter;
import com.github.ayltai.hknews.view.ModelPresenter;
import com.github.ayltai.hknews.view.Presenter;

public final class ListAdapter extends RecyclerView.Adapter<ItemViewHolder<Presenter, BaseView>> {
    private final ListPresenter presenter;
    private final boolean       isCompactStyle;

    private ItemViewHolder<Presenter, BaseView> holder;

    public ListAdapter(@Nonnull @NonNull @lombok.NonNull final ListPresenter presenter, final boolean isCompactStyle) {
        this.presenter      = presenter;
        this.isCompactStyle = isCompactStyle;
    }

    @Override
    public int getItemCount() {
        return this.presenter.getModel().isEmpty() ? 0 : 1 + this.presenter.getModel().size();
    }

    @Nonnull
    @NonNull
    @Override
    public ItemViewHolder<Presenter, BaseView> onCreateViewHolder(@Nonnull @NonNull @lombok.NonNull final ViewGroup parent, final int viewType) {
        if (this.isCompactStyle) return new ItemViewHolder<>(new ItemPresenter<>(), new CompactItemView(parent.getContext()));

        return new ItemViewHolder<>(new ItemPresenter<>(), new CozyItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<Presenter, BaseView> holder, final int position) {
        if (holder.presenter instanceof ModelPresenter)  ((ModelPresenter)holder.presenter).setModel(this.presenter.getModel().get(position));
    }

    @Override
    public void onViewAttachedToWindow(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<Presenter, BaseView> holder) {
        holder.presenter.onViewAttached(holder.view);

        if (holder.presenter instanceof ModelPresenter) ((ModelPresenter)holder.presenter).bindModel();
    }

    @Override
    public void onViewDetachedFromWindow(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<Presenter, BaseView> holder) {
        holder.presenter.onViewDetached();
    }
}
