package com.github.ayltai.hknews.config;

import javax.inject.Singleton;

import android.util.Log;

import com.github.ayltai.hknews.Constants;
import com.github.ayltai.hknews.R;
import com.github.ayltai.hknews.util.DevUtils;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

@Singleton
public final class FirebaseRemoteConfigurations implements RemoteConfigurations {
    private static FirebaseRemoteConfig config;

    public static void init() {
        if (!DevUtils.isRunningTests()) {
            FirebaseRemoteConfigurations.config = FirebaseRemoteConfig.getInstance();

            FirebaseRemoteConfigurations.config.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(Constants.MIN_FETCH_TIME)
                .build());

            FirebaseRemoteConfigurations.config.setDefaultsAsync(R.xml.configs);

            FirebaseRemoteConfigurations.config
                .fetchAndActivate()
                .addOnFailureListener(e -> {
                    if (DevUtils.isLoggable()) Log.w(FirebaseRemoteConfigurations.class.getSimpleName(), e.getMessage(), e);
                });
        }
    }

    @Override
    public int getMaxImageSize() {
        return (int)FirebaseRemoteConfigurations.config.getLong("max_image_size");
    }

    @Override
    public float getTemperature() {
        return (float)FirebaseRemoteConfigurations.config.getDouble("temperature");
    }

    @Override
    public float getLowerBound() {
        return (float)FirebaseRemoteConfigurations.config.getDouble("lower_bound");
    }

    @Override
    public boolean isPerformanceMonitoringEnabled() {
        return FirebaseRemoteConfigurations.config.getBoolean("pref_monitor_enabled");
    }
}
