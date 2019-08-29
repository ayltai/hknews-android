package com.github.ayltai.hknews.app;

import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.github.ayltai.hknews.BR;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.view.EmptyViewModel;
import com.github.ayltai.hknews.data.view.HistoryListViewModel;
import com.github.ayltai.hknews.data.view.ListViewModel;
import com.github.ayltai.hknews.databinding.FragmentListBinding;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.ViewUtils;
import com.github.ayltai.hknews.widget.CompactListAdapter;
import com.github.ayltai.hknews.widget.CozyListAdapter;
import com.github.ayltai.hknews.widget.ListAdapter;

public abstract class HistoryListFragment<C extends ViewDataBinding> extends LocalListFragment<FragmentListBinding, C> {
    public static final class Cozy extends HistoryListFragment<ViewItemCozyBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCozyBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CozyListAdapter(items);
        }
    }

    public static final class Compact extends HistoryListFragment<ViewItemCompactBinding> {
        @Nonnull
        @NonNull
        @Override
        protected ListAdapter<ViewItemCompactBinding> getListAdapter(@Nonnull @NonNull @lombok.NonNull final List<Item> items) {
            return new CompactListAdapter(items);
        }
    }

    @MenuRes
    @Override
    protected int getMenu() {
        return R.menu.history;
    }

    @Override
    protected void setUpEmptyView() {
        final EmptyViewModel model = new EmptyViewModel();
        model.setIcon(R.drawable.ic_empty_news_black_24dp);
        model.setTitle(R.string.empty_history_title);
        model.setDescription(R.string.empty_history_description);
        model.setAction(R.string.empty_history_action);

        this.binding.setVariable(BR.model, model);

        this.binding.getRoot().findViewById(R.id.empty_action).setOnClickListener(view -> {
            if (this.getContext() != null) SettingsActivity.show(this.getContext());
        });
    }

    @Nonnull
    @NonNull
    protected ListViewModel getListViewModel() {
        if (this.model == null) {
            final Activity activity = ViewUtils.getActivity(this.getContext());
            if (activity == null) throw new IllegalStateException("Unable to get Application reference");

            this.model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication())
                .create(HistoryListViewModel.class);
        }

        return this.model;
    }

    @Override
    protected int getSearchType() {
        return Constants.SEARCH_TYPE_HISTORIES;
    }

    @Override
    protected void updateSubheader() {
        this.binding.subheader.setText(this.category);
    }
}
