package com.losextraditables.bu.pictures.view.activity;

import android.animation.TimeInterpolator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.utils.WritePermissionManager;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends BuAppCompatActivity {

  @Bind(R.id.picture) ImageView imageView;

  private static final String EXTRA_IMAGE_PREVIEW_URL = "preview";
  private static final String EXTRA_IMAGE_URL = "image";
  public static final int UI_ANIMATION_DURATION = 300;
  public static final TimeInterpolator UI_ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

  private String imageUrl;
  private PhotoViewAttacher attacher;
  @Inject WritePermissionManager writePermissionManager;

  @Override protected int getLayoutId() {
    return R.layout.activity_picture;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initializeViews(savedInstanceState);
  }

  @Override protected void redirectToLogin() {

  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new PicturesModule());
  }

  public static Intent getIntentForActivity(Context context, String imageUrl) {
    Intent intent = new Intent(context, PictureActivity.class);
    intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
    return intent;
  }

  protected void initializeViews(Bundle savedInstanceState) {
    writePermissionManager.init(this);
    setupActionBar();
    attacher = new PhotoViewAttacher(imageView);
    attacher.setZoomable(true);
    imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    loadImages();
  }

  private void setupActionBar() {
    ActionBar actionBar = this.getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  private void loadImages() {
    Picasso.with(this).load(imageUrl).into(imageView);
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

  private void saveImage() {
    if (writePermissionManager.hasWritePermission()) {
      performImageDownload();
    } else {
      writePermissionManager.requestWritePermissionToUser();
    }
  }

  private void performImageDownload() {
    Uri imageUri = Uri.parse(imageUrl);
    String fileName = imageUri.getLastPathSegment();
    String downloadSubpath = getString(R.string.downloaded_pictures_subfolder) + fileName;

    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(imageUri);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDescription(imageUrl);
    request.allowScanningByMediaScanner();
    // Equivalent to request.setDestinationInExternalPublicDir(), but makes sure the Shootr subfolder exists
    request.setDestinationUri(getDownloadDestination(downloadSubpath));

    request.setMimeType("image/jpeg"); //TODO servidor debe mandarlo correctamente

    downloadManager.enqueue(request);
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

  @NonNull private Uri getDownloadDestination(String downloadSubpath) {
    File picturesFolder =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File destinationFile = new File(picturesFolder, downloadSubpath);
    destinationFile.mkdirs();
    return Uri.fromFile(destinationFile);
  }
}