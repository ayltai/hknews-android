package com.github.ayltai.hknews.app;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.github.ayltai.hknews.Components;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.media.FrescoImageLoader;
import com.github.ayltai.hknews.net.NetworkStateListener;
import com.github.ayltai.hknews.net.NetworkStateReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public final class MainActivity extends ThemedActivity implements NetworkStateListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final List<Integer> BOTTOM_TAB_FRAGMENTS = Arrays.asList(R.id.action_news_cozy, R.id.action_news_compact, R.id.action_histories_cozy, R.id.action_histories_compact, R.id.action_bookmarks_cozy, R.id.action_bookmarks_compact, R.id.action_about);

    private NavController        navController;
    private Snackbar             snackbar;
    private NetworkStateReceiver networkStateReceiver;

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.supportPostponeEnterTransition();

        this.setContentView(R.layout.activity_main);

        this.getLifecycle().addObserver(FrescoImageLoader.getInstance());

        final boolean isCompactStyle = Components.getInstance()
            .getConfigComponent()
            .userConfigurations()
            .isCompactStyle();

        this.navController = ((NavHostFragment)this.getSupportFragmentManager()
            .findFragmentById(R.id.navHostFragment))
            .getNavController();

        final NavGraph navGraph = this.navController.getNavInflater().inflate(R.navigation.main);
        navGraph.setStartDestination(isCompactStyle ? R.id.action_news_compact : R.id.action_news_cozy);
        this.navController.setGraph(navGraph);

        final BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottomNavigationView);
        MainActivity.setUpMenu(bottomNavigationView.getMenu(), isCompactStyle);
        bottomNavigationView.setOnNavigationItemReselectedListener(item -> { });
        bottomNavigationView.setSelectedItemId(isCompactStyle ? R.id.action_news_compact : R.id.action_news_cozy);

        NavigationUI.setupWithNavController(bottomNavigationView, this.navController);

        this.navController.addOnDestinationChangedListener((controller, destination, arguments) -> bottomNavigationView.setVisibility(MainActivity.BOTTOM_TAB_FRAGMENTS.contains(destination.getId()) ? View.VISIBLE : View.GONE));
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
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SettingsActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            this.finish();
            this.startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return this.navController.navigateUp();
    }

    @Override
    protected void attachBaseContext(@Nonnull @NonNull @lombok.NonNull final Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onNetworkStateChange(final boolean isOnline) {
        if (isOnline) {
            if (this.snackbar != null) {
                this.snackbar.dismiss();
                this.snackbar = null;
            }
        } else {
            if (this.snackbar == null) (this.snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.error_network, Snackbar.LENGTH_INDEFINITE)).show();
        }
    }

    private static void setUpMenu(@Nonnull @NonNull @lombok.NonNull final Menu menu, final boolean isCompactStyle) {
        menu.add(Menu.NONE, isCompactStyle ? R.id.action_news_compact : R.id.action_news_cozy, 0, R.string.title_news)
            .setIcon(R.drawable.ic_news_black_24dp);
        menu.add(Menu.NONE, isCompactStyle ? R.id.action_histories_compact : R.id.action_histories_cozy, 1, R.string.title_histories)
            .setIcon(R.drawable.ic_history_black_24dp);
        menu.add(Menu.NONE, isCompactStyle ? R.id.action_bookmarks_compact : R.id.action_bookmarks_cozy, 2, R.string.title_bookmarks)
            .setIcon(R.drawable.ic_bookmark_white_24dp);
    }
}
