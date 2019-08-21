package com.github.ayltai.hknews.app;

import javax.annotation.Nonnull;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.github.ayltai.hknews.BuildConfig;
import com.github.ayltai.hknews.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public final class VideoActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";

    private SimpleExoPlayer videoPlayer;

    public static void show(@Nullable final Context context, @Nonnull @NonNull @lombok.NonNull final String videoUrl) {
        if (context != null) context.startActivity(new Intent(context, VideoActivity.class).putExtra(VideoActivity.EXTRA_URL, videoUrl));
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_video);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.videoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory()));

        final PlayerView videoPlayerView = this.findViewById(R.id.video);
        videoPlayerView.setPlayer(this.videoPlayer);

        this.videoPlayer.prepare(new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(this, Util.getUserAgent(this, BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME))).createMediaSource(Uri.parse(this.getIntent().getStringExtra(VideoActivity.EXTRA_URL))));

        this.findViewById(R.id.exo_playback_control_view).setVisibility(View.VISIBLE);

        this.videoPlayer.setPlayWhenReady(true);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();

        this.videoPlayer.setPlayWhenReady(false);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.videoPlayer.release();
    }

    @Override
    protected void attachBaseContext(@Nonnull @NonNull @lombok.NonNull final Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
