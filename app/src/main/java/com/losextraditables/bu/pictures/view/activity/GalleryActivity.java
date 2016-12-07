package com.losextraditables.bu.pictures.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.artjimlop.altex.AltexImageDownloader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.pictures.view.adapter.GalleryAdapter;
import com.losextraditables.bu.utils.WritePermissionManager;
import com.losextraditables.bu.widgets.CustomViewPager;
import java.util.ArrayList;
import javax.inject.Inject;

public class GalleryActivity extends BuAppCompatActivity {

  private static final String EXTRA_IMAGE_URLS = "imageUrls";
  private static final String EXTRA_IMAGE_POSITION = "position";

  @Bind(R.id.pager) CustomViewPager pager;
  @Bind(R.id.av_bottom_banner) AdView mAdMobAdView;

  @Inject WritePermissionManager writePermissionManager;

  private GalleryAdapter adapter;

  public static Intent getIntentForPicturesActivity(Context context, ArrayList<String> imageUrl,
      Integer position) {
    Intent intent = new Intent(context, GalleryActivity.class);
    intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, imageUrl);
    intent.putExtra(EXTRA_IMAGE_POSITION, position);
    return intent;
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_gallery;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);
    ButterKnife.bind(this);
    writePermissionManager.init(this);
    setupActionBar();
    setupPager();
    AdRequest adRequest = new AdRequest.Builder()
            .build();
    mAdMobAdView.loadAd(adRequest);
  }

  @Override protected void redirectToLogin() {
    /* no-op */
  }

  private void setupPager() {
    adapter = new GalleryAdapter(this, getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS));
    pager.setAdapter(adapter);
    pager.setCurrentItem(getIntent().getIntExtra(EXTRA_IMAGE_POSITION, 0));
  }

  private void setupActionBar() {
    ActionBar actionBar = this.getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setDisplayShowHomeEnabled(false);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.photo_view, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    } else if (item.getItemId() == R.id.menu_download_photo) {
      saveImage();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }

  private void saveImage() {
    if (writePermissionManager.hasWritePermission()) {
      performImageDownload();
    } else {
      writePermissionManager.requestWritePermissionToUser();
    }
  }

  private void performImageDownload() {
    Uri imageUri = Uri.parse(adapter.getItemByPosition(pager.getCurrentItem()));
    String fileName = imageUri.getLastPathSegment();
    String downloadSubpath = getString(R.string.downloaded_pictures_subfolder) + fileName;
    AltexImageDownloader.writeToDisk(this, adapter.getItemByPosition(pager.getCurrentItem()),
        downloadSubpath);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == writePermissionManager.getWritePermissionRequest()) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        performImageDownload();
      } else {
        // TODO feedbackMessage.showLong(getView(), R.string.download_photo_permission_denied);
      }
    }
  }

  @OnClick(R.id.picture_background) public void onClickOutside() {
    finish();
  }

  @OnClick(R.id.download_button_container)
  public void onDownloadClicked() {
    saveImage();
  }
}
