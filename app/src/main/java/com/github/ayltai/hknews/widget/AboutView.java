package com.github.ayltai.hknews.widget;

import javax.annotation.Nonnull;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.browser.customtabs.CustomTabsIntent;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.view.AboutPresenter;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public final class AboutView extends BaseView implements AboutPresenter.View {
    private ViewGroup content;
    private AboutPage page;

    public AboutView(@Nonnull @NonNull @lombok.NonNull final Context context) {
        super(context);

        final View view = LayoutInflater.from(context).inflate(R.layout.view_about, this, false);

        this.content = view.findViewById(R.id.content);
        this.page    = new AboutPage(context);

        this.addView(view);
        this.updateLayout(BaseView.LAYOUT_SCREEN);
    }

    @Override
    public void setLogo(final int logo) {
        this.page.setImage(logo);
    }

    @Override
    public void setDescription(@Nonnull @NonNull @lombok.NonNull final String description) {
        this.page.setDescription(description);
    }

    @Override
    public void setVersion(@Nonnull @NonNull @lombok.NonNull final String version) {
        this.page.addItem(new Element()
            .setTitle(version)
            .setGravity(Gravity.CENTER_HORIZONTAL));
    }

    @Override
    public void setWebsite(@Nonnull @NonNull @lombok.NonNull final String uri, @Nonnull @NonNull @lombok.NonNull final String website) {
        this.page
            .addGroup(this.getContext().getString(R.string.contact_us))
            .addItem(new Element()
            .setTitle(website)
            .setIconDrawable(R.drawable.ic_github_black_24dp)
            .setIconTint(Components.getInstance().getConfigComponent().userConfigurations().isDarkTheme() ? R.color.textColorDark : R.color.textColorLight)
            .setOnClickListener(view -> new CustomTabsIntent.Builder()
                .build()
                .launchUrl(this.getContext(), Uri.parse(uri))));
    }

    @Override
    public void setIssues(@Nonnull @NonNull @lombok.NonNull final String uri, @Nonnull @NonNull @lombok.NonNull final String issues) {
        this.page.addItem(new Element()
            .setTitle(issues)
            .setIconDrawable(R.drawable.ic_bug_report_black_24dp)
            .setIconTint(Components.getInstance().getConfigComponent().userConfigurations().isDarkTheme() ? R.color.textColorDark : R.color.textColorLight)
            .setOnClickListener(view -> new CustomTabsIntent.Builder()
                .build()
                .launchUrl(this.getContext(), Uri.parse(uri))));
    }

    @UiThread
    @Override
    public void show() {
        if (this.content.getChildCount() == 0) this.content.addView(this.page.create());
    }
}
