package com.losextraditables.bu.videos.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.repository.VideoRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class GetVideosUseCase extends RosieUseCase {

  private final VideoRepository videoRepository;

  @Inject
  public GetVideosUseCase(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  @UseCase
  public void getVideos(String uid) throws Exception {
    notifySuccess(getVideosFromUser(uid));
  }

  protected Observable<List<Video>> getVideosFromUser(String uid) throws Exception {
    return videoRepository.getVideos(uid);
  }
}
