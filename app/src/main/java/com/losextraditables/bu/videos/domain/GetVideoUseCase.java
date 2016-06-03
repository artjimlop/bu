package com.losextraditables.bu.videos.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.repository.VideoRepository;
import javax.inject.Inject;
import rx.Observable;

public class GetVideoUseCase extends RosieUseCase {

  private final VideoRepository videoRepository;

  @Inject public GetVideoUseCase(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  @UseCase
  public void obtainVideo(String url) {
    Observable<Video> videoObservable = videoRepository.getVideo(url);
    notifySuccess(videoObservable);
  }
}
