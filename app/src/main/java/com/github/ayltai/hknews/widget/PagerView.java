package com.github.ayltai.hknews.widget;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Category;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.util.ContextUtils;
import com.github.ayltai.hknews.view.PagerPresenter;
import com.google.android.material.tabs.TabLayout;

public class PagerView extends BaseView implements PagerPresenter.View, TabLayout.OnTabSelectedListener, Cacheable {
    //region Components

    private ProgressBar progressBar;
    private ViewGroup   container;
    private TabLayout   tabLayout;
    private ViewPager   viewPager;

    //endregion

    private PagerAdapter adapter;

    public PagerView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        this.setBackgroundColor(ContextUtils.getColor(context, R.attr.windowBackground));

        final View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_pager, this, false);

        this.progressBar = view.findViewById(R.id.progressBar);
        this.container   = view.findViewById(R.id.container);
        this.tabLayout   = view.findViewById(R.id.tabLayout);
        this.viewPager   = view.findViewById(R.id.viewPager);

        this.addView(view);
        this.updateLayout(BaseView.LAYOUT_SCREEN);
    }

    @Override
    public void setCategoryNames(@Nonnull @NonNull @lombok.NonNull final List<String> categories) {
        final List<String> categoryNames = new ArrayList<>();
        for (final String category : categories) {
            final String categoryName = Category.getDisplayName(category);
            if (!categoryNames.contains(categoryName)) categoryNames.add(categoryName);
        }

        for (final String categoryName : categoryNames) this.tabLayout.addTab(this.tabLayout.newTab().setText(categoryName));

        this.viewPager.setAdapter(this.adapter = this.createPagerAdapter());
        this.tabLayout.setupWithViewPager(this.viewPager);

        this.container.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void refresh() {
        if (this.adapter != null) this.adapter.refresh(this.viewPager.getCurrentItem());
    }

    @Override
    public void clear() {
        if (this.adapter != null) this.adapter.clear(this.viewPager.getCurrentItem());
    }

    @Override
    public void filter(@Nullable final CharSequence text) {
        if (this.adapter != null) this.adapter.filter(text);
    }

    @Nonnull
    @NonNull
    protected PagerAdapter createPagerAdapter() {
        return new PagerAdapter((PagerPresenter)this.presenter);
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.tabLayout.addOnTabSelectedListener(this);
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.tabLayout.removeOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(@Nonnull @NonNull @lombok.NonNull final TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(@Nonnull @NonNull @lombok.NonNull final TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(@Nonnull @NonNull @lombok.NonNull final TabLayout.Tab tab) {
    }
}
