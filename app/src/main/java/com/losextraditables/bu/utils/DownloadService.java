package com.losextraditables.bu.utils;

import com.koushikdutta.ion.ProgressCallback;

public interface DownloadService {
  void donwload(String url, ProgressCallback progressCallback);
}
