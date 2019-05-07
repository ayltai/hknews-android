package com.github.ayltai.hknews.view;

import javax.annotation.Nonnull;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.github.ayltai.hknews.R;

public final class AboutPresenter extends BasePresenter<AboutPresenter.View> {
    public interface View extends Presenter.View {
        void setLogo(@DrawableRes int logo);

        void setDescription(@Nonnull @NonNull @lombok.NonNull String description);

        void setVersion(@Nonnull @NonNull @lombok.NonNull String version);

        void setWebsite(@Nonnull @NonNull @lombok.NonNull String uri, @Nonnull @NonNull @lombok.NonNull String website);

        void setIssues(@Nonnull @NonNull @lombok.NonNull String uri, @Nonnull @NonNull @lombok.NonNull String issues);

        @UiThread
        void show();
    }

    private boolean isInitialized;

    @CallSuper
    @Override
    public void onViewAttached(@Nonnull @NonNull @lombok.NonNull final AboutPresenter.View view) {
        super.onViewAttached(view);

        if (!this.isInitialized) {
            this.isInitialized = true;

            view.setLogo(R.drawable.logo);
            view.setDescription(view.getContext().getString(R.string.app_description));

            try {
                view.setVersion(String.format(view.getContext().getString(R.string.app_version), view.getContext().getPackageManager().getPackageInfo(view.getContext().getPackageName(), 0).versionName));
            } catch (final PackageManager.NameNotFoundException e) {
                Log.w(e.getMessage(), e);
            }

            view.setWebsite(view.getContext().getString(R.string.app_website_uri), view.getContext().getString(R.string.app_website));
            view.setIssues(view.getContext().getString(R.string.app_issue_uri), view.getContext().getString(R.string.app_issue));

            view.show();
        }
    }
}
