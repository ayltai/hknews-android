package com.github.ayltai.hknews;

import javax.annotation.Nonnull;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.ayltai.hknews.media.FrescoImageLoader;
import com.github.ayltai.hknews.net.NetworkStateListener;
import com.github.ayltai.hknews.net.NetworkStateReceiver;
import com.github.ayltai.hknews.view.Router;
import com.google.android.material.snackbar.Snackbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public final class MainActivity extends ThemedActivity implements NetworkStateListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Router               router;
    private Snackbar             snackbar;
    private NetworkStateReceiver networkStateReceiver;

    //region Lifecycle management

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        this.getLifecycle().addObserver(FrescoImageLoader.getInstance());
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();

        this.networkStateReceiver = new NetworkStateReceiver();
        this.networkStateReceiver.addListener(this);

        this.registerReceiver(this.networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();

        if (this.networkStateReceiver != null) {
            this.networkStateReceiver.removeListener(this);

            this.unregisterReceiver(this.networkStateReceiver);
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.getLifecycle().removeObserver(FrescoImageLoader.getInstance());

        Components.getInstance()
            .getMediaComponent()
            .centerFinder()
            .dispose();

        if (!this.router.isDisposed()) {
            this.router.dispose();
            this.router = null;
        }
    }

    //endregion

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        this.router.handleActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @CallSuper
    @Override
    public void onBackPressed() {
        if (!this.router.handleBack()) super.onBackPressed();
    }

    @Override
    public void onNetworkStateChange(final boolean isOnline) {
        if (isOnline) {
            if (this.snackbar != null) {
                this.snackbar.dismiss();
                this.snackbar = null;
            }
        } else {
            if (this.snackbar == null) {
                this.snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.error_network, Snackbar.LENGTH_INDEFINITE);
                this.snackbar.show();
            }
        }
    }

    @Override
    protected void attachBaseContext(@Nonnull @NonNull @lombok.NonNull final Context newBase) {
        if (this.router == null) this.router = Components.getInstance()
            .getRouterComponent(this)
            .router();

        super.attachBaseContext(this.router.attachNewBase(ViewPumpContextWrapper.wrap(newBase)));
    }
}
