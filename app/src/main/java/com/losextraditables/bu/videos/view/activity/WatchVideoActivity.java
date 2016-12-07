package com.losextraditables.bu.videos.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.koushikdutta.ion.ProgressCallback;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.utils.DownloadService;
import com.losextraditables.bu.utils.WritePermissionManager;
import com.losextraditables.bu.videos.VideosModule;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class WatchVideoActivity extends BuAppCompatActivity {

  private static final String EXTRA_VIDEO_IMAGE = "videoImage";
  private static final String EXTRA_VIDEO_URL = "videoURL";
  private static final String EXTRA_VIDEO_TITLE = "videoTitle";
  private static final String EMPTY_STRING = "";
  @Inject DownloadService downloadService;
  @Inject
  WritePermissionManager writePermissionManager;
  @Bind(R.id.video_title) TextView title;
  @Bind(R.id.video) JCVideoPlayerStandard videoPlayer;
  @Bind(R.id.download_button) TextView donwloadView;
  @Bind(R.id.download_loading) ProgressBar loadingView;
  @Bind(R.id.av_bottom_banner) AdView mAdMobAdView;
  private String videoUrl;

  public static Intent getIntentForPicturesActivity(Context context, VideoModel videoModel) {
    Intent intent = new Intent(context, WatchVideoActivity.class);
    intent.putExtra(EXTRA_VIDEO_IMAGE, videoModel.getImage());
    intent.putExtra(EXTRA_VIDEO_URL, videoModel.getUrl());
    intent.putExtra(EXTRA_VIDEO_TITLE, videoModel.getTitle());
    return intent;
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_watch_video;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    writePermissionManager.init(this);
    renderVideo();
    AdRequest adRequest = new AdRequest.Builder()
            .build();
    mAdMobAdView.loadAd(adRequest);
  }

  @Override protected void redirectToLogin() {

  }

  public void renderVideo() {
    String image = getIntent().getStringExtra(EXTRA_VIDEO_IMAGE);
    videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);
    String videoTitle = getIntent().getStringExtra(EXTRA_VIDEO_TITLE);
    title.setText(videoTitle);
    if (image != null) {
      Picasso.with(this).load(image).into(videoPlayer.thumbImageView);
    }
    videoPlayer.setUp(videoUrl, EMPTY_STRING);
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Collections.singletonList((Object) new VideosModule());
  }

  @OnClick(R.id.picture_background) public void onClickOutside() {
    finish();
  }

  @OnClick(R.id.download_button) public void onDownloadClicked() {
    if (writePermissionManager.hasWritePermission()) {
      performVideoDownload();
    } else {
      writePermissionManager.requestWritePermissionToUser();
    }
  }

  private void performVideoDownload() {
    loadingView.setVisibility(View.VISIBLE);
    donwloadView.setVisibility(View.GONE);
    downloadService.donwload(videoUrl, new ProgressCallback() {
      @Override
      public void onProgress(long downloaded, long total) {
        donwloadView.setVisibility(View.VISIBLE);
        donwloadView.setText("" + (downloaded / total) * 100 + "%");
      }
    });
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }
}
