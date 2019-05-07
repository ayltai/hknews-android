package com.github.ayltai.hknews.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.github.ayltai.hknews.util.Irrelevant;
import com.github.ayltai.hknews.util.RxUtils;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.view.Presenter;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class BaseView extends FrameLayout implements Presenter.View {
    //region Constants

    protected static final int LAYOUT_DEFAULT    = 0;
    protected static final int LAYOUT_SCREEN     = 1;
    protected static final int LAYOUT_MAX_WIDTH  = 2;
    protected static final int LAYOUT_MAX_HEIGHT = 3;

    //endregion

    @IntDef({ BaseView.LAYOUT_DEFAULT, BaseView.LAYOUT_SCREEN, BaseView.LAYOUT_MAX_WIDTH, BaseView.LAYOUT_MAX_HEIGHT })
    protected @interface Layout {}

    //region Variables

    protected final FlowableProcessor<Irrelevant> attaches = PublishProcessor.create();
    protected final FlowableProcessor<Irrelevant> detaches = PublishProcessor.create();

    private final List<Disposable> disposables = Collections.synchronizedList(new ArrayList<>());

    protected Presenter<?> presenter;

    //endregion

    public BaseView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @Override
    public <V extends Presenter.View> void setPresenter(@Nullable final Presenter<V> presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public Activity getActivity() {
        return ViewUtils.getActivity(this);
    }

    @Nullable
    @Override
    public LifecycleOwner getLifecycleOwner() {
        final Activity activity = this.getActivity();

        if (activity == null) return null;
        if (activity instanceof LifecycleOwner) return (LifecycleOwner)activity;

        return null;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> attaches() {
        return this.attaches;
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<Irrelevant> detaches() {
        return this.detaches;
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.attaches.onNext(Irrelevant.INSTANCE);
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        RxUtils.resetDisposables(this.disposables);

        super.onDetachedFromWindow();

        this.detaches.onNext(Irrelevant.INSTANCE);
    }

    protected void updateLayout(@BaseView.Layout final int layout) {
        this.setLayoutParams(new FrameLayout.LayoutParams(layout == BaseView.LAYOUT_SCREEN || layout == BaseView.LAYOUT_MAX_WIDTH ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, layout == BaseView.LAYOUT_SCREEN || layout == BaseView.LAYOUT_MAX_HEIGHT ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    protected void manageDisposable(@Nonnull @NonNull @lombok.NonNull final Disposable disposable) {
        this.disposables.add(disposable);
    }
}
