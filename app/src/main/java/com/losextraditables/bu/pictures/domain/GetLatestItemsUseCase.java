package com.losextraditables.bu.pictures.domain;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class GetLatestItemsUseCase extends RosieUseCase{
  private final PictureRepository pictureRepository;

  @Inject
  public GetLatestItemsUseCase(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  @UseCase
  public void getPictures() throws Exception {
    notifySuccess(getPicturesFromUser());
  }

  protected Observable<List<Picture>> getPicturesFromUser() throws Exception {
    //crear entidad que encapsule videos y pictures
    return pictureRepository.getLatestItems();
  }
}
