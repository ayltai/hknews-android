package com.github.ayltai.hknews.app;

import javax.annotation.Nonnull;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.data.view.AboutViewModel;
import com.github.ayltai.hknews.databinding.FragmentAboutBinding;

public final class AboutFragment extends BaseFragment {
    private FragmentAboutBinding binding;

    @Nonnull
    @NonNull
    @Override
    public View onCreateView(@Nonnull @NonNull @lombok.NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        this.binding = DataBindingUtil.bind(view);

        return view;
    }

    @LayoutRes
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
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

        final AboutViewModel model = new ViewModelProvider.NewInstanceFactory().create(AboutViewModel.class);
        model.setLogo(R.drawable.logo);
        model.setDescription(R.string.app_description);
        model.setWebsite(R.string.app_website);
        model.setWebsiteUrl(R.string.app_website_uri);
        model.setIssues(R.string.app_issues);
        model.setIssuesUrl(R.string.app_issues_uri);

        try {
            model.setVersion(String.format(this.getString(R.string.app_version), this.getContext().getPackageManager().getPackageInfo(this.getContext().getPackageName(), 0).versionName));
        } catch (final PackageManager.NameNotFoundException e) {
            Log.w(e.getMessage(), e);
        }

        this.binding.setModel(model);

        this.view.findViewById(R.id.website).setOnClickListener(this.getOnClickListener(this.getString(model.getWebsiteUrl())));
        this.view.findViewById(R.id.issues).setOnClickListener(this.getOnClickListener(this.getString(model.getIssuesUrl())));
    }

    @Nullable
    private View.OnClickListener getOnClickListener(@Nonnull @NonNull @lombok.NonNull final String uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        return this.getActivity() == null || intent.resolveActivity(this.getActivity().getPackageManager()) == null ? null : view -> this.startActivity(intent);
    }
}
