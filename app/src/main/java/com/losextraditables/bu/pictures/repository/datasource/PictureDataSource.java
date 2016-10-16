package com.losextraditables.bu.pictures.repository.datasource;

import com.losextraditables.bu.pictures.domain.model.Picture;
import java.util.List;
import rx.Observable;

public interface PictureDataSource {

  Observable<Picture> getPictureFromScrap(String url);

  Observable<Void> savePicture(Picture url, String uid);

  Observable<List<Picture>> getPictures(String uid);

  Observable<Void> removePicture(String uid, String position);

  Observable<List<Picture>> getLatestItems();
}
