package com.losextraditables.bu.pictures.repository;

import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.datasource.ServicePictureDataSource;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PictureRepository {

  private final ServicePictureDataSource pictureDataSource;

  @Inject public PictureRepository(ServicePictureDataSource pictureDataSource) {
    this.pictureDataSource = pictureDataSource;
  }

  public Observable<Picture> getPicture(String url) {
    return pictureDataSource.getPictureFromScrap(url);
  }

  public Observable<Void> savePicture(Picture picture, String uid) {
    return pictureDataSource.savePicture(picture, uid);
  }

  public Observable<List<Picture>> getPictures(String uid) {
    return pictureDataSource.getPictures(uid);
  }
}
