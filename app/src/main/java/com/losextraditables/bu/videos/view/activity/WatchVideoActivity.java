package com.losextraditables.bu.videos.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.pictures.view.activity.GalleryActivity;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.squareup.picasso.Picasso;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class WatchVideoActivity extends BuAppCompatActivity {

  private static final String EXTRA_VIDEO_IMAGE = "videoImage";
  private static final String EXTRA_VIDEO_URL = "videoURL";
  private static final String EXTRA_VIDEO_TITLE = "videoTitle";
  private static final String EMPTY_STRING = "";
  @Bind(R.id.video_title) TextView title;
  @Bind(R.id.video) JCVideoPlayerStandard videoPlayer;

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
    renderVideo();
  }

  @Override protected void redirectToLogin() {

  }

  public void renderVideo() {
    String image = getIntent().getStringExtra(EXTRA_VIDEO_IMAGE);
    String url = getIntent().getStringExtra(EXTRA_VIDEO_URL);
    String videoTitle = getIntent().getStringExtra(EXTRA_VIDEO_TITLE);
    title.setText(videoTitle);
    if (image != null) {
      Picasso.with(this).load(image).into(videoPlayer.thumbImageView);
    }
    videoPlayer.setUp(url, EMPTY_STRING);
  }

  @OnClick(R.id.picture_background) public void onClickOutside() {
    finish();
  }
}
