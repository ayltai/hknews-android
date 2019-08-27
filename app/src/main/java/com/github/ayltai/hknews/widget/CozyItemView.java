package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.app.ListFragment$CompactDirections;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.databinding.ViewItemCozyBinding;
import com.github.ayltai.hknews.util.ViewUtils;

public final class CozyItemView extends ItemView<ViewItemCozyBinding> {
    public CozyItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @LayoutRes
    @Override
    protected int getLayoutId() {
        return R.layout.view_item_cozy;
    }

    @Override
    public void bind(@Nonnull @NonNull @lombok.NonNull final Item item) {
        this.binding.setItem(item);

        final Activity activity = ViewUtils.getActivity(this);
        if (activity != null) this.setOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigate(ListFragment$CompactDirections.detailsAction(item.getUrl())));
    }
}
