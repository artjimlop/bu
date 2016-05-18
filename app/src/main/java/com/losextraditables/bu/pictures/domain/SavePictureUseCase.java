package com.losextraditables.bu.pictures.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import javax.inject.Inject;
import rx.Observable;

public class SavePictureUseCase extends RosieUseCase {

  private final PictureRepository pictureRepository;

  @Inject public SavePictureUseCase(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  @UseCase
  public void savePicture(Picture picture, String uid) {
    Observable<Void> pictureObservable = pictureRepository.savePicture(picture, uid);
    notifySuccess(pictureObservable);
  }
}
