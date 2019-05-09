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

    @Override
    public float getMinFaceSize() {
        return (float)FirebaseRemoteConfigurations.config.getDouble("min_face_size");
    }

    @Override
    public int getAccuracyForFaceDetection() {
        return (int)FirebaseRemoteConfigurations.config.getLong("accuracy_for_face_detection");
    }

    @Override
    public int getMaxImageSizeForFaceDetection() {
        return (int)FirebaseRemoteConfigurations.config.getLong("max_image_size_for_face_detection");
    }

    @Override
    public boolean isPerformanceMonitoringEnabled() {
        return FirebaseRemoteConfigurations.config.getBoolean("pref_monitor_enabled");
    }
}
