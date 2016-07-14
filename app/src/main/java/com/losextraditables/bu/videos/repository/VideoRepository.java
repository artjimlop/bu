package com.losextraditables.bu.videos.repository;

import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.repository.datasource.VideoDataSource;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VideoRepository {

  private final VideoDataSource videoDataSource;

  @Inject public VideoRepository(VideoDataSource videoDataSource) {
    this.videoDataSource = videoDataSource;
  }

  public Observable<Video> getVideo(String url) {
    return videoDataSource.getVideoFromScrap(url);
  }

  public Observable<Void> saveVideo(Video video, String uid) {
    return videoDataSource.saveVideo(video, uid);
  }

  public Observable<List<Video>> getVideos(String uid) {
    return videoDataSource.getVideos(uid);
  }
}
