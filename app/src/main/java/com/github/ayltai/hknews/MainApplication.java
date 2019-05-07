package com.github.ayltai.hknews;

import java.util.Collections;

import android.os.StrictMode;

import androidx.annotation.CallSuper;

import com.akaita.java.rxjava2debug.RxJava2Debug;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.github.ayltai.hknews.config.ConfigModule;
import com.github.ayltai.hknews.media.DaggerMediaComponent;
import com.github.ayltai.hknews.media.FrescoImageLoader;
import com.github.ayltai.hknews.net.DaggerNetComponent;
import com.github.ayltai.hknews.util.DevUtils;
import com.github.piasy.biv.BigImageViewer;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public final class MainApplication extends BaseApplication {
    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();

        RxJava2Debug.enableRxJava2AssemblyTracking(new String[] { BuildConfig.APPLICATION_ID });

        this.applyDevMode();

        ConfigModule.init(this);

        this.initFabric();
        this.initFresco();
        this.initBigImageViewer();
        this.initCalligraphy();
    }

    private void applyDevMode() {
        if (!BuildConfig.DEBUG && !DevUtils.isRunningTests()) {
            StrictMode.setThreadPolicy(DevUtils.newThreadPolicy());
            StrictMode.setVmPolicy(DevUtils.newVmPolicy());

            if (!LeakCanary.isInAnalyzerProcess(this) && !DevUtils.isRunningTests()) LeakCanary.install(this);
        }
    }

    private void initFabric() {
        if (!DevUtils.isLoggable() && !DevUtils.isRunningTests()) Fabric.with(
            this,
            new Answers(),
            new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                    .disabled(DevUtils.isLoggable())
                    .build())
                .build());
    }

    private void initFresco() {
        FLog.setMinimumLoggingLevel(DevUtils.isLoggable() ? FLog.INFO : FLog.ERROR);

        ImagePipelineConfig.getDefaultImageRequestConfig().setProgressiveRenderingEnabled(true);

        if (!DevUtils.isRunningUnitTest()) Fresco.initialize(this, OkHttpImagePipelineConfigFactory.newBuilder(this, DaggerNetComponent.create()
            .okHttpClient())
            .setDownsampleEnabled(true)
            .setResizeAndRotateEnabledForNetwork(true)
            .setExecutorSupplier(new DefaultExecutorSupplier(Runtime.getRuntime().availableProcessors()))
            .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
            .setRequestListeners(Collections.singleton(new RequestLoggingListener()))
            .build());
    }

    private void initBigImageViewer() {
        FrescoImageLoader.init(this);
        BigImageViewer.initialize(DaggerMediaComponent.create().imageLoader());
    }

    private void initCalligraphy() {
        ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()))
            .build());
    }
}
