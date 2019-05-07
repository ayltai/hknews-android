package com.github.ayltai.hknews;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ayltai.hknews.config.DaggerConfigComponent;

public abstract class ThemedActivity extends AppCompatActivity {
    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTheme(DaggerConfigComponent.create().userConfigurations().isDarkTheme() ? this.getDarkTheme() : this.getLightTheme());
    }

    @StyleRes
    protected abstract int getDarkTheme();

    @StyleRes
    protected abstract int getLightTheme();
}
