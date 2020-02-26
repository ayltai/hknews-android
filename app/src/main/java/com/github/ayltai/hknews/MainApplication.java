package com.github.ayltai.hknews;

import java.util.Collections;

import android.os.StrictMode;

import androidx.annotation.CallSuper;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

import com.akaita.java.rxjava2debug.RxJava2Debug;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.github.ayltai.hknews.config.ConfigModule;
import com.github.ayltai.hknews.media.FrescoImageLoader;
import com.github.ayltai.hknews.media.TensorFlowCenterFinder;
import com.github.ayltai.hknews.util.DevUtils;
import com.github.piasy.biv.BigImageViewer;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.perf.FirebasePerformance;
import com.squareup.leakcanary.LeakCanary;

public final class MainApplication extends BaseApplication {
    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();

        RxJava2Debug.enableRxJava2AssemblyTracking(new String[] { BuildConfig.APPLICATION_ID });

        this.applyDevMode();

        ConfigModule.init(this);

        if (!DevUtils.isLoggable() && !DevUtils.isRunningTests()) FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        this.initFresco();
        this.initBigImageViewer();
        this.initCalligraphy();

        if (!DevUtils.isRunningTests()) FirebasePerformance.getInstance()
            .setPerformanceCollectionEnabled(Components.getInstance()
                .getConfigComponent()
                .remoteConfigurations()
                .isPerformanceMonitoringEnabled());

        TensorFlowCenterFinder.init(this);
    }

    private void applyDevMode() {
        if (!BuildConfig.DEBUG && !DevUtils.isRunningTests()) {
            StrictMode.setThreadPolicy(DevUtils.newThreadPolicy());
            StrictMode.setVmPolicy(DevUtils.newVmPolicy());

            if (!LeakCanary.isInAnalyzerProcess(this) && !DevUtils.isRunningTests()) LeakCanary.install(this);
        }
    }

    private void initFresco() {
        FLog.setMinimumLoggingLevel(DevUtils.isLoggable() ? FLog.INFO : FLog.ERROR);

        ImagePipelineConfig.getDefaultImageRequestConfig().setProgressiveRenderingEnabled(true);

        if (!DevUtils.isRunningUnitTest()) Fresco.initialize(this, OkHttpImagePipelineConfigFactory.newBuilder(this, Components.getInstance()
            .getNetComponent()
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
        BigImageViewer.initialize(Components.getInstance().getMediaComponent().imageLoader());
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
