package com.github.ayltai.hknews;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

public final class SplashActivity extends ThemedActivity {
    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @StyleRes
    @Override
    protected int getDarkTheme() {
        return R.style.SplashTheme_Dark;
    }

    @StyleRes
    @Override
    protected int getLightTheme() {
        return R.style.SplashTheme_Light;
    }
}
