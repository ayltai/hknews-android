package com.github.ayltai.hknews.app;

import java.util.Date;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.app.Application;
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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import io.reactivex.disposables.CompositeDisposable;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.repository.Repository;
import com.github.ayltai.hknews.data.view.DetailsViewModel;
import com.github.ayltai.hknews.databinding.FragmentDetailsBinding;
import com.github.ayltai.hknews.util.IntentUtils;
import com.github.ayltai.hknews.util.RxUtils;

public final class DetailsFragment extends BaseFragment {
    //region Variables

    private FragmentDetailsBinding binding;
    private String                 itemUrl;
    private DetailsViewModel       model;
    private CompositeDisposable    disposables;
    private String                 title;
    private boolean                isBookmarked;

    //endregion

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
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = this.getActivity();
        if (activity != null) {
            final Application application = activity.getApplication();
            if (application != null) {
                this.model = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DetailsViewModel.class);

                final Date now = new Date();

                this.disposables.add(this.model
                    .setLastAccess(this.itemUrl, now)
                    .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
                    .doOnSuccess(item -> Components.getInstance()
                        .getConfigComponent()
                        .userConfigurations()
                        .setLastAccessedDate(item.getCategory().getName(), now))
                    .compose(RxUtils.applySingleBackgroundToMainSchedulers())
                    .subscribe(
                        item -> {
                            this.title        = item.getTitle();
                            this.isBookmarked = Boolean.TRUE.equals(item.getIsBookmarked());

                            this.binding.setItem(item);

                            final MenuItem bookmarkMenuItem = this.toolbar.getMenu().findItem(R.id.action_bookmark);
                            if (bookmarkMenuItem != null) bookmarkMenuItem.setIcon(this.isBookmarked ? R.drawable.ic_bookmark_filled_white_24dp : R.drawable.ic_bookmark_white_24dp);
                        },
                        RxUtils::handleError
                    ));
            }
        }
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
            this.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            this.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(activity, R.id.navHostFragment).navigateUp());
        }
    }

    @Override
    protected void setUpMenuItems() {
        final Menu     menu             = this.toolbar.getMenu();
        final MenuItem bookmarkMenuItem = menu.findItem(R.id.action_bookmark);
        final MenuItem viewMenuItem     = menu.findItem(R.id.action_view);
        final MenuItem shareMenuItem    = menu.findItem(R.id.action_share);

        if (bookmarkMenuItem != null) {
            bookmarkMenuItem.setOnMenuItemClickListener(item -> {
                this.disposables.add(this.model
                    .setIsBookmarked(this.itemUrl, !this.isBookmarked)
                    .compose(RxUtils.applySingleSchedulers(Repository.SCHEDULER))
                    .doOnSuccess(i -> Components.getInstance()
                        .getConfigComponent()
                        .userConfigurations()
                        .setLastBookmarkedDate(i.getCategory().getName(), new Date()))
                    .compose(RxUtils.applySingleBackgroundToMainSchedulers())
                    .subscribe(
                        i -> {
                            this.isBookmarked = Boolean.TRUE.equals(i.getIsBookmarked());

                            bookmarkMenuItem.setIcon(this.isBookmarked ? R.drawable.ic_bookmark_filled_white_24dp : R.drawable.ic_bookmark_white_24dp);
                        },
                        RxUtils::handleError
                    ));

                return true;
            });
        }

        if (viewMenuItem != null) viewMenuItem.setOnMenuItemClickListener(item -> {
            IntentUtils.viewUrl(this.getActivity(), this.itemUrl);

            return true;
        });

        if (shareMenuItem != null) shareMenuItem.setOnMenuItemClickListener(item -> {
            IntentUtils.shareUrl(this.getActivity(), this.itemUrl, this.title);

            return true;
        });
    }
}
