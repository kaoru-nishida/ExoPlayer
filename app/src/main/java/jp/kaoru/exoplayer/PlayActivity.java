package jp.kaoru.exoplayer;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayActivity extends AppCompatActivity implements Player.EventListener, SimpleExoPlayer.VideoListener {

    private static final String TAG = "PlayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        hideSystemUI();

        // 1. デフォルトのTrackSelectorを作成
        // 再生中の帯域幅を測定。必須でない場合はnull
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. プレーヤーを作成
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        // 3. リスナーを追加
        player.addListener(this);
        player.addVideoListener(this);

        // 4. プレーヤーをViewに紐付ける
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.simpleExoPlayerView);
        simpleExoPlayerView.setPlayer(player);

        // 5. メディアデータがロードされるDataSourceインスタンスを生成
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this.getApplicationContext(), this.getPackageName()), bandwidthMeter);
        //User-AgentはStringで書くことも可能

        // 6. メディアデータを解析するためのExtractorインスタンスを生成
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = null;
        // 7. これは、再生されるメディアを表すMediaSource
        switch (this.getIntent().getStringExtra("type")) {
            case "OTHER":
                videoSource = new ExtractorMediaSource(Uri.parse(this.getIntent().getStringExtra("url")),
                        dataSourceFactory, extractorsFactory, null, null);
                break;
            case "HLS":
                videoSource = new HlsMediaSource(Uri.parse(this.getIntent().getStringExtra("url")),
                        dataSourceFactory, null, null);
                break;
        }
        // 8. ソースを持つプレーヤーを準備
        player.prepare(videoSource);
    }

    private void hideSystemUI() {
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }


    // TimeLineが変更された時
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    // トラックが変更された時
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    // 読み込みが変更された時
    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    // プレーヤーの状態が変更された時
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        hideSystemUI();
    }

    // 繰り返し状態が変更された時
    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    // エラーが発生した時
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    // 位置の不連続性が検出された時
    @Override
    public void onPositionDiscontinuity() {

    }

    // 再生パラメータが変更された時
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        hideSystemUI();
    }

    // ビデオのサイズが変更された時(VideoListener)
    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    // 最初のフレームをレンダリングされた時(VideoListener)
    @Override
    public void onRenderedFirstFrame() {
        hideSystemUI();
    }
}
