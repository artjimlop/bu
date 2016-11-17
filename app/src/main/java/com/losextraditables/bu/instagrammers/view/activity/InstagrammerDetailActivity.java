package com.losextraditables.bu.instagrammers.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.instagrammers.view.adapter.OnProfilePictureClickListener;
import com.losextraditables.bu.instagrammers.view.adapter.ProfilePicturesAdapter;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammerDetailPresenter;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.utils.BlurTransform;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import javax.inject.Inject;

public class InstagrammerDetailActivity extends BuAppCompatActivity
    implements AppBarLayout.OnOffsetChangedListener, InstagrammerDetailPresenter.View,
    View.OnClickListener {

  public static final String USERNAME = "username";
  public static final String PHOTO = "photo";
  public static final String URL = "url";

  private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
  private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
  private static final int ALPHA_ANIMATIONS_DURATION = 200;

  @Bind(R.id.instagrammer_avatar) CircleImageView userPhoto;
  @Bind(R.id.instagrammer_blur_avatar) ImageView blurImage;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.username) TextView instagrammerName;
  @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @Bind(R.id.app_bar) AppBarLayout appBarLayout;
  @Bind(R.id.toolbar_username) TextView toolbarUsername;
  @Bind(R.id.title_container) LinearLayout titleContainer;
  @Bind(R.id.pictures_list) RecyclerView pictureList;
  @Bind(R.id.stalin_info_card) CardView cardView;

  @Inject
  @Presenter InstagrammerDetailPresenter instagrammerDetailPresenter;

  private boolean mIsTheTitleVisible = false;
  private boolean mIsTheTitleContainerVisible = true;
  private InstagrammerModel instagrammerModel;

  public static void init(Activity activity, View sharedView, InstagrammerModel instagrammerModel) {
    Intent intent = new Intent(activity, InstagrammerDetailActivity.class);
    intent.putExtra(USERNAME, instagrammerModel.getUserName());
    intent.putExtra(PHOTO, instagrammerModel.getProfilePicture());
    intent.putExtra(URL, instagrammerModel.getWebsite());

    handleActivityVersion(activity, sharedView, intent);
  }

  private static void handleActivityVersion(Activity activity, View sharedView, Intent intent) {
    activity.startActivity(intent);
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //      Pair<View, String> imagePair = new Pair<>(sharedView, sharedView.getTransitionName());
    //      ActivityOptions activityOptions =
    //          ActivityOptions.makeSceneTransitionAnimation(activity, imagePair);
    //      activity.startActivity(intent, activityOptions.toBundle());
    //    } else {
    //      activity.startActivity(intent);
    //    }
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
    instagrammerDetailPresenter.getProfilePictures(getIntent().getStringExtra(URL));
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_instagrammer_detail;
  }

  @Override protected void redirectToLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override protected void onPreparePresenter() {
    super.onPreparePresenter();
    instagrammerDetailPresenter.initialize();
  }

  private void setupToolbar() {
    String photo = getIntent().getStringExtra(PHOTO);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    Picasso.with(this).load(photo).into(userPhoto);
    Picasso.with(this).load(photo).transform(new BlurTransform(this)).into(blurImage);
    toolbar.setOnClickListener(this);
    userPhoto.setOnClickListener(this);
  }

  private void setupUsername() {
    String username = getIntent().getStringExtra(USERNAME);
    instagrammerName.setText(username);
    toolbarUsername.setText(username);
    instagrammerName.setOnClickListener(this);
  }

  private void setupAppBarLayout() {
    final String username = getIntent().getStringExtra(USERNAME);
    appBarLayout.addOnOffsetChangedListener(this);
  }

  private void setupStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.setStatusBarColor(getResources().getColor(R.color.textColorPrimary));
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

      if (!mIsTheTitleVisible) {
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
      if (mIsTheTitleContainerVisible) {
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

  private static void startAlphaAnimation(View v, long duration, int visibility) {
    AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
        ? new AlphaAnimation(0f, 1f)
        : new AlphaAnimation(1f, 0f);

    alphaAnimation.setDuration(duration);
    alphaAnimation.setFillAfter(true);
    v.startAnimation(alphaAnimation);
  }

  @OnClick(R.id.stalin_info_card)
  public void onInstagramInfoCardClicked() {
    goToInstagram();
  }

  private void goToInstagram() {
    String url = getIntent().getStringExtra(URL);
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    startActivity(i);
  }

  @Override public void renderProfilePictures(List<String> pictures) {
    cardView.setVisibility(View.GONE);
    pictureList.setVisibility(View.VISIBLE);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    ProfilePicturesAdapter profilePicturesAdapter = new ProfilePicturesAdapter(this,
        new OnProfilePictureClickListener() {
          @Override public void onItemClick(String pictureUrl) {
            goToSavedPictureActivity(pictureUrl);
          }
        });
    profilePicturesAdapter.setItems(pictures);
    if (pictureList != null) {
      pictureList.setLayoutManager(linearLayoutManager);
      pictureList.setHasFixedSize(true);
      pictureList.setAdapter(profilePicturesAdapter);
    }
  }

  @Override public void showEmpty() {
    cardView.setVisibility(View.VISIBLE);
    pictureList.setVisibility(View.GONE);
  }

  @Override public void showGenericError() {
    /* no-op */
  }

  @Override public void showConnectionError() {
    /* no-op */
  }

  @Override public void hideLoading() {
    //TODO
  }

  @Override public void showLoading() {
    //TODO
  }

  @Override public void onClick(View view) {
    goToInstagram();
  }

  private void goToSavedPictureActivity(String url) {
    PictureActivity.init(this, null, url);
    overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }
}
