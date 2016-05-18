package com.losextraditables.bu.pictures.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import javax.inject.Inject;
import rx.Observable;

public class GetPictureUseCase extends RosieUseCase {

  private final PictureRepository pictureRepository;

  @Inject public GetPictureUseCase(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  @UseCase
  public void obtainPicture(String url, String uid) {
    Observable<Picture> pictureObservable = pictureRepository.getPicture(url);
    notifySuccess(pictureObservable);
  }
}
