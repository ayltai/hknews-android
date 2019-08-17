package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;

public final class CompactListAdapter extends ListAdapter<ViewItemCompactBinding> {
    public CompactListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        super(items);
    }

    @Nonnull
    @NonNull
    @Override
    public ItemViewHolder<ItemView<ViewItemCompactBinding>> onCreateViewHolder(@Nonnull @NonNull @lombok.NonNull final ViewGroup parent, final int viewType) {
        return new ItemViewHolder<>(new CompactItemView(parent.getContext()));
    }
}
