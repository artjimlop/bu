package com.losextraditables.bu.videos.repository.datasource;

import com.losextraditables.bu.videos.Video;
import java.util.List;
import rx.Observable;

public interface VideoDataSource {
  Observable<Video> getVideoFromScrap(String url);

  Observable<Void> saveVideo(Video video, String uid);

  Observable<List<Video>> getVideos(String uid);
}
