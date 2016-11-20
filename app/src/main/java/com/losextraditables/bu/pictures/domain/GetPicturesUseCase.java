package com.losextraditables.bu.pictures.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class GetPicturesUseCase extends RosieUseCase {

  private final PictureRepository pictureRepository;

  @Inject
  public GetPicturesUseCase(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  @UseCase
  public void getPictures(String uid) throws Exception {
    notifySuccess(getPicturesFromUser(uid));
  }

  protected Observable<List<Picture>> getPicturesFromUser(String uid) throws Exception {
    return pictureRepository.getPictures(uid);
  }
}
