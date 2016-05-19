package com.losextraditables.bu.instagrammers.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.utils.BlurTransform;
import com.squareup.picasso.Picasso;

public class InstagrammerDetailActivity extends AppCompatActivity {

  public static final String USERNAME = "username";
  public static final String PHOTO = "photo";
  @Bind(R.id.instagrammer_avatar)
  ImageView userPhoto;
  @Bind(R.id.instagrammer_blur_avatar) ImageView blurImage;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @Bind(R.id.instagrammer_container) View container;

  private InstagrammerModel instagrammerModel;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void init(Activity activity, View sharedView, InstagrammerModel instagrammerModel) {
    Intent intent = new Intent(activity, InstagrammerDetailActivity.class);
    intent.putExtra(USERNAME, instagrammerModel.getUserName());
    intent.putExtra(PHOTO, instagrammerModel.getProfilePicture());
    Pair<View, String> imagePair = new Pair<>(sharedView, sharedView.getTransitionName());

    ActivityOptions activityOptions =
        ActivityOptions.makeSceneTransitionAnimation(activity,imagePair);
    activity.startActivity(intent, activityOptions.toBundle());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_instagrammer_detail);
    ButterKnife.bind(this);
    setupToolbar();
  }

  private void setupToolbar() {
    String photo = getIntent().getStringExtra(PHOTO);
    setupToolbarTitle();
    setSupportActionBar(toolbar);
    Picasso.with(this).load(photo).into(userPhoto);
    Picasso.with(this).load(photo).transform(new BlurTransform(this)).into(blurImage);
  }

  private void setupToolbarTitle() {
    String username = getIntent().getStringExtra(USERNAME);
    toolbar.setTitle(username);
  }
}
