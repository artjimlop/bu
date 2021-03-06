package com.losextraditables.bu.pictures.domain.model;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import javax.inject.Inject;
import rx.Observable;

public class RemovePictureUserCase extends RosieUseCase {

  private final PictureRepository pictureRepository;

  @Inject
  public RemovePictureUserCase(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  @UseCase
  public void removePicture(String uid, String url) throws Exception {
    notifySuccess(removeUserPicture(uid, url));
  }

  protected Observable<Void> removeUserPicture(String uid, String url) throws Exception {
    return pictureRepository.removePicture(uid, url);
  }
}
