package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.data.model.Item;

public abstract class ListAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<ItemViewHolder<ItemView<B>>> {
    private final List<Item> items;

    public ListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@Nonnull @NonNull @lombok.NonNull final ItemViewHolder<ItemView<B>> holder, final int position) {
        holder.view.bind(this.items.get(position));
    }
}
