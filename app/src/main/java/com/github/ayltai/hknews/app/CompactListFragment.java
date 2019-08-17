package com.github.ayltai.hknews.app;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

public class CompactListFragment extends ListFragment<ViewItemCompactBinding> {
    @Nonnull
    @NonNull
    @Override
    protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
        if (this.adapter == null) this.adapter = new CompactListAdapter(items);
        return this.adapter;
    }
}
