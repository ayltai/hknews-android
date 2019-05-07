package com.github.ayltai.hknews.data;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.util.DevUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import dagger.Module;
import dagger.Provides;

@Module
public final class DataModule {
    private static final int SCHEMA_VERSION = 1;

    private static boolean isInitialized;

    private final Context context;

    public DataModule(@Nonnull @NonNull @lombok.NonNull final Context context) {
        this.context = context.getApplicationContext();
    }

    @Nonnull
    @NonNull
    @Provides
    Realm provideRealm() {
        if (!DataModule.isInitialized) {
            if (!DevUtils.isRunningUnitTest()) {
                Realm.init(this.context);

                Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                    .schemaVersion(DataModule.SCHEMA_VERSION)
                    .deleteRealmIfMigrationNeeded()
                    .compactOnLaunch()
                    .build());
            }

            DataModule.isInitialized = true;
        }

        return Realm.getDefaultInstance();
    }
}
