package com.github.ayltai.hknews.app;

import java.util.Date;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import io.reactivex.disposables.CompositeDisposable;

import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.view.DetailsViewModel;
import com.github.ayltai.hknews.databinding.FragmentDetailsBinding;
import com.github.ayltai.hknews.util.RxUtils;

public final class DetailsFragment extends BaseFragment {
    private FragmentDetailsBinding binding;
    private String                 itemUrl;
    private DetailsViewModel       model;
    private CompositeDisposable    disposables;
    private boolean                isBookmarked;

    @CallSuper
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.disposables = new CompositeDisposable();
    }

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

        this.disposables.dispose();
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

        final Activity activity = this.getActivity();
        if (activity != null) {
            final Application application = activity.getApplication();
            if (application != null) {
                this.model = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DetailsViewModel.class);

                this.disposables.add(this.model
                    .get(this.itemUrl)
                    .flatMap(item -> this.model.setLastAccess(this.itemUrl, new Date()))
                    .subscribe(
                        item -> {
                            this.isBookmarked = item.isBookmarked();

                            this.binding.setItem(item);
                        },
                        RxUtils::handleError
                    ));
            }
        }
    }

    @CallSuper
    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

        this.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        final AppCompatActivity activity = (AppCompatActivity)this.getActivity();
        if (activity != null) this.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigateUp());
    }

    @Override
    protected void setUpMenuItems() {
        final Menu     menu             = this.toolbar.getMenu();
        final MenuItem bookmarkMenuItem = menu.findItem(R.id.action_bookmark);
        final MenuItem viewMenuItem     = menu.findItem(R.id.action_view);
        final MenuItem shareMenuItem    = menu.findItem(R.id.action_share);

        if (bookmarkMenuItem != null) bookmarkMenuItem.setOnMenuItemClickListener(item -> {
            this.disposables.add(this.model
                .setIsBookmarked(this.itemUrl, !this.isBookmarked)
                .subscribe(
                    irrelevant -> {
                        this.isBookmarked = !this.isBookmarked;

                        bookmarkMenuItem.setIcon(this.isBookmarked ? R.drawable.ic_bookmark_filled_white_24dp : R.drawable.ic_bookmark_white_24dp);
                    },
                    RxUtils::handleError
                ));

            return true;
        });

        if (viewMenuItem != null) viewMenuItem.setOnMenuItemClickListener(item -> {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.itemUrl));

            if (this.getActivity() == null || intent.resolveActivity(this.getActivity().getPackageManager()) == null) return false;

            this.startActivity(intent);

            return true;
        });

        if (shareMenuItem != null) shareMenuItem.setOnMenuItemClickListener(item -> {
            // TODO: Implements share

            return true;
        });
    }
}
