package com.github.ayltai.hknews;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.takisoft.preferencex.PreferenceFragmentCompat;

public final class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferencesFix(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        this.setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();

        this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();

        this.getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(@Nonnull @NonNull @lombok.NonNull final SharedPreferences sharedPreferences, @Nonnull @NonNull @lombok.NonNull final String key) {
        if (this.getActivity() != null) this.getActivity().setResult(Activity.RESULT_OK);
    }
}
