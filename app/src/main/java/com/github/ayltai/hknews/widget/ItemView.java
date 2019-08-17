package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.github.ayltai.hknews.data.model.Item;

public abstract class ItemView<B extends ViewDataBinding> extends DataBindingView<B, Item> {
    protected ItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }
}
