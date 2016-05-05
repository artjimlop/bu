package com.losextraditables.bu.pictures.repository.datasource;

import rx.Observable;

public interface PictureDataSource {

  Observable<String> getPictureFromScrap(String url);

  Observable<Void> savePicture(String url, String uid);
}
