package com.losextraditables.bu.login.repository;

import com.losextraditables.bu.login.repository.datasource.UserDatasource;
import com.losextraditables.bu.login.repository.datasource.UserDatasourceFactory;
import javax.inject.Inject;
import rx.Observable;

public class UserRepository {

  private final UserDatasource userDatasource;

  @Inject public UserRepository(UserDatasourceFactory factory) {
    userDatasource = factory.createDatasource();
  }

  public Observable<Void> createUser(String username, String password) {
    return userDatasource.createUser(username, password);
  }

  public Observable<Void> login(String username, String password) {
    return userDatasource.login(username, password);
  }
}
