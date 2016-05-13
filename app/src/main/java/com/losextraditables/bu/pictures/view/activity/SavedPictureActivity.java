package com.losextraditables.bu.pictures.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.squareup.picasso.Picasso;

public class SavedPictureActivity extends AppCompatActivity {

  @Bind(R.id.saved_picture) ImageView savedPictureView;
  @Bind(R.id.toolbar) Toolbar toolbar;

  public static final String EXTRA_IMAGE_URL = "url";

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
  }

  private void loadImage() {
    Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).into(savedPictureView);
  }
}
