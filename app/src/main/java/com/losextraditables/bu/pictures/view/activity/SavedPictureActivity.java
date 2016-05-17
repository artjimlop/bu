package com.losextraditables.bu.pictures.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SavedPictureActivity extends AppCompatActivity {
  public static final String EXTRA_IMAGE_URL = "url";
  private static final int UI_ANIMATION_DURATION = 10000;

  @Bind(R.id.saved_picture) ImageView savedPictureView;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.saved_picture_container) RelativeLayout savedPictureContainer;

  public static Intent getIntentForActivity(Context context, String imageUrl) {
    Intent intent = new Intent(context, SavedPictureActivity.class);
    intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_saved_picture);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    loadImage();
    setupImageZoom();
    setupBackgroundAnimation();
  }

  private void setupImageZoom() {
    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(savedPictureView);
    photoViewAttacher.setZoomable(true);
  }

  private void setupBackgroundAnimation() {
    Drawable backgrounds[] = new Drawable[2];
    Resources res = getResources();
    backgrounds[0] = res.getDrawable(android.R.color.background_dark);
    backgrounds[1] = res.getDrawable(R.color.colorPrimaryDark);

    TransitionDrawable transitionDrawable = new TransitionDrawable(backgrounds);
    savedPictureContainer.setBackground(transitionDrawable);
    transitionDrawable.startTransition(UI_ANIMATION_DURATION);
  }

  private void loadImage() {
    Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).into(savedPictureView);
  }
}
