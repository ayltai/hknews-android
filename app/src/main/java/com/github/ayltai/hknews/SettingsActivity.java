package com.github.ayltai.hknews;

import javax.annotation.Nonnull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.Toolbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public final class SettingsActivity extends ThemedActivity {
    public static final int REQUEST_CODE = 1;

    public static void show(@Nonnull @NonNull @lombok.NonNull final Context context) {
        if (context instanceof Activity) {
            ((Activity)context).startActivityForResult(new Intent(context, SettingsActivity.class), SettingsActivity.REQUEST_CODE);
        } else {
            context.startActivity(new Intent(context, SettingsActivity.class));
        }
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setResult(Activity.RESULT_CANCELED);

        this.setContentView(R.layout.activity_settings);

        final Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_settings);
        toolbar.setNavigationOnClickListener(view -> this.finish());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void attachBaseContext(@Nonnull @NonNull @lombok.NonNull final Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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
