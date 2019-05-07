package com.github.ayltai.hknews;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.ayltai.hknews.media.FrescoImageLoader;
import com.github.ayltai.hknews.view.DaggerRouterComponent;
import com.github.ayltai.hknews.view.Router;
import com.github.ayltai.hknews.view.RouterModule;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public final class MainActivity extends ThemedActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    Router router;

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        this.getLifecycle().addObserver(FrescoImageLoader.getInstance());
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.getLifecycle().removeObserver(FrescoImageLoader.getInstance());

        if (!this.router.isDisposed()) this.router.dispose();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        this.router.handleActivityResult(requestCode, resultCode, data);
    }

    @CallSuper
    @Override
    public void onBackPressed() {
        if (!this.router.handleBack()) super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(@Nonnull @NonNull @lombok.NonNull final Context newBase) {
        if (this.router == null) DaggerRouterComponent.builder()
            .routerModule(new RouterModule(this))
            .build()
            .inject(this);

        super.attachBaseContext(this.router.attachNewBase(ViewPumpContextWrapper.wrap(newBase)));
    }

    @StyleRes
    @Override
    protected int getDarkTheme() {
        return R.style.AppTheme_Dark;
    }

    @StyleRes
    @Override
    protected int getLightTheme() {
        return R.style.AppTheme_Light;
    }
}
