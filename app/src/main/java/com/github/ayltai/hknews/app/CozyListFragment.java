package com.github.ayltai.hknews.app;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

public class CozyListFragment extends ListFragment<ViewItemCozyBinding> {
    @Nonnull
    @NonNull
    @Override
    protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        if (this.adapter == null) this.adapter = new CozyListAdapter(items);
        return this.adapter;
    }
}
