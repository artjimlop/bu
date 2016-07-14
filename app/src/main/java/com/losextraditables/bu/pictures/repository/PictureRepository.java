package com.losextraditables.bu.pictures.repository;

import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.repository.datasource.PictureDataSource;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PictureRepository {

  private final PictureDataSource pictureDataSource;

  @Inject public PictureRepository(PictureDataSource pictureDataSource) {
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

  public Observable<Void> removePicture(String uid, Integer position) {
    return pictureDataSource.removePicture(uid, position);
  }
}
