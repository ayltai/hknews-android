package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.view.Presenter;

public final class ItemViewHolder<P extends Presenter, V extends View> extends RecyclerView.ViewHolder {
    public final P presenter;
    public final V view;

    public ItemViewHolder(@Nonnull @NonNull @lombok.NonNull final P presenter, @Nonnull @NonNull @lombok.NonNull final View itemView) {
        super(itemView);

        this.presenter = presenter;
        this.view      = (V)itemView;
    }
}
