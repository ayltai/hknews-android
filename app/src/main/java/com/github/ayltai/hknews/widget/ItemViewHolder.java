package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public final class ItemViewHolder<V extends View> extends RecyclerView.ViewHolder {
    public final V view;

    public ItemViewHolder(@Nonnull @NonNull @lombok.NonNull final View itemView) {
        super(itemView);

        this.view = (V)itemView;
    }
}
