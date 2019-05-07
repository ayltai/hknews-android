package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.github.ayltai.hknews.util.Irrelevant;

import io.reactivex.Flowable;

public interface Presenter<V extends Presenter.View> {
    interface View {
        <V extends Presenter.View> void setPresenter(@Nullable Presenter<V> presenter);

        @Nonnull
        @NonNull
        Context getContext();

        @Nullable
        Activity getActivity();

        @Nullable
        LifecycleOwner getLifecycleOwner();

        @Nonnull
        @NonNull
        Flowable<Irrelevant> attaches();

        @Nonnull
        @NonNull
        Flowable<Irrelevant> detaches();

        @CallSuper
        void onAttachedToWindow();

        @CallSuper
        void onDetachedFromWindow();
    }

    interface Factory<P extends Presenter<V>, V extends Presenter.View> {
        boolean isSupported(@Nonnull @NonNull @lombok.NonNull Object key);

        @Nonnull
        @NonNull
        P createPresenter();

        @Nonnull
        @NonNull
        V createView(@Nonnull @NonNull @lombok.NonNull Context context);
    }

    @Nullable
    V getView();

    void onViewAttached(@Nonnull @NonNull @lombok.NonNull V view);

    void onViewDetached();
}
