package com.losextraditables.bu.instagrammers.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.utils.BlurTransform;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class InstagrammerDetailActivity extends AppCompatActivity
    implements AppBarLayout.OnOffsetChangedListener {

  public static final String USERNAME = "username";
  public static final String PHOTO = "photo";

  private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
  private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
  private static final int ALPHA_ANIMATIONS_DURATION              = 200;

  @Bind(R.id.instagrammer_avatar) CircleImageView userPhoto;
  @Bind(R.id.instagrammer_blur_avatar) ImageView blurImage;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.username) TextView instagrammerName;
  @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @Bind(R.id.app_bar) AppBarLayout appBarLayout;
  @Bind(R.id.toolbar_username) TextView toolbarUsername;
  @Bind(R.id.title_container) LinearLayout titleContainer;

  private boolean mIsTheTitleVisible          = false;
  private boolean mIsTheTitleContainerVisible = true;
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
    setupAppBarLayout();
    setupUsername();
    setupStatusBarColor();
    startAlphaAnimation(toolbarUsername, 0, View.INVISIBLE);
  }

  private void setupToolbar() {
    String photo = getIntent().getStringExtra(PHOTO);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    Picasso.with(this).load(photo).into(userPhoto);
    Picasso.with(this).load(photo).transform(new BlurTransform(this)).into(blurImage);
  }

  private void setupUsername() {
    String username = getIntent().getStringExtra(USERNAME);
    instagrammerName.setText(username);
    toolbarUsername.setText(username);
  }

  private void setupAppBarLayout() {
    final String username = getIntent().getStringExtra(USERNAME);
    appBarLayout.addOnOffsetChangedListener(this);
  }

  private void setupStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }
  }


  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
    int maxScroll = appBarLayout.getTotalScrollRange();
    float percentage = (float) Math.abs(offset) / (float) maxScroll;

    handleAlphaOnTitle(percentage);
    handleToolbarTitleVisibility(percentage);
  }

  private void handleToolbarTitleVisibility(float percentage) {
    if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

      if(!mIsTheTitleVisible) {
        startAlphaAnimation(toolbarUsername, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
        mIsTheTitleVisible = true;
      }

    } else {

      if (mIsTheTitleVisible) {
        startAlphaAnimation(toolbarUsername, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
        mIsTheTitleVisible = false;
      }
    }
  }

  private void handleAlphaOnTitle(float percentage) {
    if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
      if(mIsTheTitleContainerVisible) {
        startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
        mIsTheTitleContainerVisible = false;
      }

    } else {

      if (!mIsTheTitleContainerVisible) {
        startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
        mIsTheTitleContainerVisible = true;
      }
    }
  }

  private static void startAlphaAnimation (View v, long duration, int visibility) {
    AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
        ? new AlphaAnimation(0f, 1f)
        : new AlphaAnimation(1f, 0f);

    alphaAnimation.setDuration(duration);
    alphaAnimation.setFillAfter(true);
    v.startAnimation(alphaAnimation);
  }
}
