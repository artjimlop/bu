package com.losextraditables.bu.videos.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.videos.repository.VideoRepository;
import javax.inject.Inject;
import rx.Observable;

public class RemoveVideoUseCase extends RosieUseCase {

  private final VideoRepository videoRepository;

  @Inject public RemoveVideoUseCase(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  @UseCase
  public void removeVideo(String uid, String url) {
    Observable<Void> videoObservable = videoRepository.removeVideo(uid, url);
    notifySuccess(videoObservable);
  }
}
