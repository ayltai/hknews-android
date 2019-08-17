package com.github.ayltai.hknews.app;

import java.util.Date;

import javax.annotation.Nonnull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.repository.ItemRepository;
import com.github.ayltai.hknews.databinding.FragmentDetailsBinding;
import com.github.ayltai.hknews.util.RxUtils;

import io.reactivex.disposables.Disposable;

public final class DetailsFragment extends BaseFragment {
    private FragmentDetailsBinding binding;
    private String                 itemUrl;
    private Disposable             disposable;

    @CallSuper
    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final Bundle bundle = this.getArguments();
        this.itemUrl = bundle == null || !bundle.containsKey(Constants.ARG_ITEM_URL) ? null : bundle.getString(Constants.ARG_ITEM_URL);

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        this.binding = DataBindingUtil.bind(view);

        return view;
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.disposable.dispose();
    }

    @LayoutRes
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    @IdRes
    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @CallSuper
    @Override
    protected void init() {
        super.init();

        final ItemRepository repository = ItemRepository.create(Components.getInstance()
            .getDataComponent(this.getActivity())
            .realm());

        this.disposable = repository.get(this.itemUrl)
            .flatMap(item -> {
                item.setLastAccessed(new Date());

                return repository.set(item);
            })
            .subscribe(
                item -> this.binding.setItem(item),
                RxUtils::handleError
            );
    }

    @CallSuper
    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

        this.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);


        final AppCompatActivity activity = (AppCompatActivity)this.getActivity();
        if (activity != null) {
            activity.setSupportActionBar(this.toolbar);

            this.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigateUp());
        }
    }
}
