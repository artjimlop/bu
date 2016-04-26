package com.losextraditables.bu.pictures.repository;

import com.losextraditables.bu.pictures.repository.datasource.PictureScrapDataSource;
import javax.inject.Inject;
import rx.Observable;

public class PictureRepository {

  private final PictureScrapDataSource pictureDataSource;

  @Inject public PictureRepository(PictureScrapDataSource pictureDataSource) {
    this.pictureDataSource = pictureDataSource;
  }

  public Observable<String> getPicture(String url) {
    return pictureDataSource.getPictureFromScrap(url);
  }
}
