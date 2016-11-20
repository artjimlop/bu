package com.losextraditables.bu.pictures.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.artjimlop.altex.AltexImageDownloader;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.utils.WritePermissionManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends BuAppCompatActivity {

  @Bind(R.id.picture) ImageView imageView;

  private static final String EXTRA_IMAGE_URL = "image";
  private static final String EXTRA_REFRESH = "refreshPictures";
  private String imageUrl;
  private PhotoViewAttacher attacher;
  @Bind(R.id.download_button_container) View downloadView;
  @Inject WritePermissionManager writePermissionManager;

  public static void init(Activity activity, View sharedView, String imageUrl) {
    Intent intent = new Intent(activity, PictureActivity.class);
    intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
    handleActivityVersion(activity, sharedView, intent);
  }

  private static void handleActivityVersion(Activity activity, View sharedView, Intent intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sharedView != null) {
      ActivityOptions activityOptions =
          ActivityOptions.makeSceneTransitionAnimation(activity, sharedView,
              sharedView.getTransitionName());
      activity.startActivity(intent, activityOptions.toBundle());
    } else {
      activity.startActivity(intent);
    }
  }

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

  public static Intent getIntentForPicturesActivity(Context context, String imageUrl) {
    Intent intent = new Intent(context, PictureActivity.class);
    intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
    intent.putExtra(EXTRA_REFRESH, true);
    return intent;
  }

  public static Intent getIntentForActivity(Context context, String imageUrl) {
    Intent intent = new Intent(context, PictureActivity.class);
    intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
    intent.putExtra(EXTRA_REFRESH, false);
    return intent;
  }

  protected void initializeViews(Bundle savedInstanceState) {
    writePermissionManager.init(this);
    attacher = new PhotoViewAttacher(imageView);
    attacher.setZoomable(true);
    imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    loadImages();
    // TODO: Replace this test id with your personal ad unit id
    //moPubView.setAdUnitId("07c97f5fdd84408e8f05871b0d5874ea");
    //moPubView.loadAd();
  }

  private void loadImages() {
    Picasso.with(this).load(imageUrl).into(imageView, new Callback() {
      @Override public void onSuccess() {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView, true);
        attacher.update();
      }

      @Override public void onError() {
        /* no-op */
      }
    });
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
    AltexImageDownloader.writeToDisk(this, imageUrl, downloadSubpath);
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

  @Override protected void onDestroy() {
    super.onDestroy();
    //moPubView.destroy();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    if (getIntent().getBooleanExtra(EXTRA_REFRESH, false)) {
      startActivity(MainTabbedActivity.getIntentForActivity(this));
      overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
    }
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }

  @OnClick(R.id.download_button_container)
  public void onDownloadClicked() {
    saveImage();
  }

  @OnClick(R.id.picture_background) public void onClickOutside() {
    finish();
  }
}
