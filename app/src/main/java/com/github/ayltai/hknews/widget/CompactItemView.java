package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.databinding.ViewItemCompactBinding;
import com.github.ayltai.hknews.util.ViewUtils;

public final class CompactItemView extends ItemView<ViewItemCompactBinding> {
    public CompactItemView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);
    }

    @LayoutRes
    @Override
    protected int getLayoutId() {
        return R.layout.view_item_compact;
    }

    @Override
    public void bind(@Nonnull @NonNull @lombok.NonNull final Item item) {
        this.binding.setItem(item);

        final Activity activity = ViewUtils.getActivity(this);
        if (activity != null) {
            final Bundle bundle = new Bundle();
            bundle.putString(Constants.ARG_ITEM_URL, item.getUrl());

            this.setOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigate(R.id.action_details, bundle));
        }
    }
}
