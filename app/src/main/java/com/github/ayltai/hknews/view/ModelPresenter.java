package com.github.ayltai.hknews.view;

import androidx.annotation.UiThread;

import lombok.Getter;
import lombok.Setter;

public abstract class ModelPresenter<M, V extends Presenter.View> extends BasePresenter<V> {
    @Getter
    @Setter
    private M model;

    @UiThread
    public abstract void bindModel();
}
