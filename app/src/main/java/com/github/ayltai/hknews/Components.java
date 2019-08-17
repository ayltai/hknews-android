package com.github.ayltai.hknews;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.config.ConfigComponent;
import com.github.ayltai.hknews.config.DaggerConfigComponent;
import com.github.ayltai.hknews.data.DaggerDataComponent;
import com.github.ayltai.hknews.data.DataComponent;
import com.github.ayltai.hknews.data.DataModule;
import com.github.ayltai.hknews.diagnostics.DaggerDiagnosticsComponent;
import com.github.ayltai.hknews.diagnostics.DiagnosticsComponent;
import com.github.ayltai.hknews.media.DaggerMediaComponent;
import com.github.ayltai.hknews.media.MediaComponent;
import com.github.ayltai.hknews.net.DaggerNetComponent;
import com.github.ayltai.hknews.net.NetComponent;

public final class Components {
    private static Components instance = new Components();

    //region Variables

    private ConfigComponent      configComponent;
    private DataComponent        dataComponent;
    private DiagnosticsComponent diagnosticsComponent;
    private MediaComponent       mediaComponent;
    private NetComponent         netComponent;

    //endregion

    public static Components getInstance() {
        return Components.instance;
    }

    @Nonnull
    @NonNull
    public ConfigComponent getConfigComponent() {
        if (this.configComponent == null) this.configComponent = DaggerConfigComponent.create();

        return this.configComponent;
    }

    @Nonnull
    @NonNull
    public DataComponent getDataComponent(@Nonnull @NonNull @lombok.NonNull final Context context) {
        if (this.dataComponent == null) this.dataComponent = DaggerDataComponent.builder()
            .dataModule(new DataModule(context))
            .build();

        return this.dataComponent;
    }

    @Nonnull
    @NonNull
    public DiagnosticsComponent getDiagnosticsComponent() {
        if (this.diagnosticsComponent == null) this.diagnosticsComponent = DaggerDiagnosticsComponent.create();

        return this.diagnosticsComponent;
    }

    @Nonnull
    @NonNull
    public MediaComponent getMediaComponent() {
        if (this.mediaComponent == null || this.mediaComponent.centerFinder().isDisposed()) this.mediaComponent = DaggerMediaComponent.create();

        return this.mediaComponent;
    }

    @Nonnull
    @NonNull
    public NetComponent getNetComponent() {
        if (this.netComponent == null) this.netComponent = DaggerNetComponent.create();

        return this.netComponent;
    }
}
