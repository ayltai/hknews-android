package com.github.ayltai.hknews.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.disposables.Disposable;

import lombok.Getter;

public class BasePresenter<V extends Presenter.View> implements Presenter<V> {
    private final List<Disposable> disposables = Collections.synchronizedList(new ArrayList<>());

    @Getter
    private V view;

    protected void manageDisposable(@Nonnull @NonNull @lombok.NonNull final Disposable disposable) {
        this.disposables.add(disposable);
    }

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final V view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @CallSuper
    @Override
    public void onViewDetached() {
        if (this.view != null) {
            this.view.setPresenter(null);
            this.view = null;
        }

        RxUtils.resetDisposables(this.disposables);
    }
}
