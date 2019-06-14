package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.view.CategoryPresenter;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public final class CategoryView extends BaseView implements CategoryPresenter.View {
    private final FlowableProcessor<String> selects = PublishProcessor.create();

    private final NavigationView navigationView;

    public CategoryView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_category, this, false);

        this.navigationView = view.findViewById(R.id.navigationView);

        this.addView(view);
    }

    @Override
    public void setCategoryNames(@Nonnull @NonNull @lombok.NonNull final List<String> categoryNames) {
        MenuItem firstItem = null;

        for (final String categoryName : categoryNames) {
            final MenuItem item = this.navigationView.getMenu().add(categoryName);
            item.setCheckable(true);

            if (firstItem == null) firstItem = item;
        }

        this.navigationView.setNavigationItemSelectedListener(item -> {
            this.navigationView.setCheckedItem(item);

            this.selects.onNext(item.getTitle().toString());

            return true;
        });

        this.navigationView.setCheckedItem(firstItem);
        this.selects.onNext(firstItem.getTitle().toString());
    }

    @Nonnull
    @NonNull
    @Override
    public Flowable<String> selects() {
        return this.selects;
    }
}
