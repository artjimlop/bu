package com.losextraditables.bu.videos.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.repository.VideoRepository;
import javax.inject.Inject;
import rx.Observable;

public class SaveVideoUseCase extends RosieUseCase {

  private final VideoRepository videoRepository;

  @Inject public SaveVideoUseCase(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  @UseCase
  public void saveVideo(Video picture, String uid) {
    Observable<Void> videoObservable = videoRepository.saveVideo(picture, uid);
    notifySuccess(videoObservable);
  }
}
