package com.github.ayltai.hknews.widget;

import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.util.Cacheable;
import com.github.ayltai.hknews.view.FeaturedListPresenter;

public final class FeaturedListView extends BaseView implements FeaturedListPresenter.View, Cacheable {
    private RecyclerView recyclerView;

    public FeaturedListView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_list_featured, this, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        new LinearSnapHelper().attachToRecyclerView(this.recyclerView);

        this.addView(view);
    }

    @Override
    public void setItems(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        this.recyclerView.setAdapter(new FeaturedListAdapter(items));
    }
}
