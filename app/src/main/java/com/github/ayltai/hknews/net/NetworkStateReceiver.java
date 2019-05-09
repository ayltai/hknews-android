package com.github.ayltai.hknews.net;

import java.util.Set;

import javax.annotation.Nonnull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.github.ayltai.hknews.util.NetworkUtils;

public final class NetworkStateReceiver extends BroadcastReceiver {
    private final Set<NetworkStateListener> listeners = new ArraySet<>();

    public void addListener(@Nonnull @NonNull @lombok.NonNull final NetworkStateListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(@Nonnull @NonNull @lombok.NonNull final NetworkStateListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void onReceive(@Nonnull @NonNull @lombok.NonNull final Context context, @Nullable final Intent intent) {
        final boolean isOnline = NetworkUtils.isOnline(context);

        for (final NetworkStateListener listener : this.listeners) listener.onNetworkStateChange(isOnline);
    }
}
