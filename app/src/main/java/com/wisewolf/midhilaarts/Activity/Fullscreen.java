package com.wisewolf.midhilaarts.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.R;


public class Fullscreen extends AppCompatActivity implements  Player.EventListener{

    ProgressDialog mProgressDialog;
    ProgressBar progressBar;

    PlayerView exoplayerView;

    private String mVedioUrl;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    long curr;
    int init=0;

    ImageView fullscreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setKeepScreenOn( this.getWindow(), true);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        exoplayerView=findViewById(R.id.exo_full);
        progressBar=findViewById(R.id.progressBar_full);

        fullscreenButton = exoplayerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent=getIntent();
          curr=Integer.valueOf(intent.getStringExtra("current"));
        String vURL=intent.getStringExtra("url");
        media(vURL);

    }

    public   void setKeepScreenOn(Window window, boolean isOn) {

        if (window == null) {
            return;
        }
        if (isOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    void media(String videoUrl) {
        mVedioUrl=videoUrl;
        setUp();

    }

    private void setUp() {
        initializePlayer();
        if (mVedioUrl == null) {
            return;
        }
        buildMediaSource(Uri.parse(mVedioUrl));
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            exoplayerView.setPlayer(player);
        }
    }
    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
       /* DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
       */
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, getString(R.string.app_name)),
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        );


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
            null /* listener */,
            httpDataSourceFactory
        );
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.setPlayWhenReady(true);
        player.addListener(Fullscreen.this);

    }
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }
    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }
    @Override
    public void onLoadingChanged(boolean isLoading) {
    }
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                endedFunct();
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:
                progressBar.setVisibility(View.GONE);
                if (init==0) {
                    player.seekTo(curr);
                    init=1;
                }
                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }
    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }
    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }
    @Override
    public void onPositionDiscontinuity(int reason) {
    }
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }
    @Override
    public void onSeekProcessed() {
    }

    private void endedFunct() {

        if (GlobalData.logged){
            if (!GlobalData.Logged_user.getPlantype().equals("")){
                int videoNum=Integer.valueOf(GlobalData.planTypeMod.getVideoNo());
                if (GlobalData.completed>videoNum) {
                    alertComplete();

                }

            }
        }
    }

    private void alertComplete() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Fullscreen.this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alertcomplete, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatePlan();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updatePlan() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plan_type").document(GlobalData.Logged_user.getId())
            .update("video_no", String.valueOf(GlobalData.completed));
        GlobalData.planTypeMod.setVideoNo(String.valueOf(GlobalData.completed));
        finish();

    }
}
