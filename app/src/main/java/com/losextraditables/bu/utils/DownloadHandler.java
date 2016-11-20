package com.losextraditables.bu.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import com.koushikdutta.ion.ProgressCallback;
import com.losextraditables.bu.R;
import java.io.File;
import javax.inject.Inject;

public class DownloadHandler implements DownloadService {

  private final Context context;

  @Inject public DownloadHandler(Context context) {
    this.context = context;
  }

  @Override public void donwload(String url, ProgressCallback progressCallback) {
    Uri imageUri = Uri.parse(url);
    String fileName = imageUri.getLastPathSegment();
    String downloadSubpath = context.getString(R.string.downloaded_videos_subfolder) + fileName;

    DownloadManager downloadManager =
        (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(imageUri);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDescription(url);
    request.allowScanningByMediaScanner();
    request.setDestinationUri(getDownloadDestination(downloadSubpath));

    downloadManager.enqueue(request);
  }

  @NonNull private Uri getDownloadDestination(String downloadSubpath) {
    File picturesFolder =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    File destinationFile = new File(picturesFolder, downloadSubpath);
    destinationFile.mkdirs();
    return Uri.fromFile(destinationFile);
  }
}
