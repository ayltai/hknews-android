package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class DataBindingView<B extends ViewDataBinding, T> extends FrameLayout {
    protected final B binding;

    protected DataBindingView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        this.binding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), this.getLayoutId(), this, true);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public abstract void bind(@Nonnull @NonNull @lombok.NonNull T model);
}
