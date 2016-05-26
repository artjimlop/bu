package com.losextraditables.bu.login.repository;

import com.losextraditables.bu.login.repository.datasource.UserDatasource;
import javax.inject.Inject;
import rx.Observable;

public class UserRepository {

  private final UserDatasource userDatasource;

  @Inject public UserRepository(UserDatasource userDatasource) {
    this.userDatasource = userDatasource;
  }

  public Observable<Void> createUser(String username, String password) {
    return userDatasource.createUser(username, password);
  }

  public Observable<String> login(String username, String password) {
    return userDatasource.login(username, password);
  }

  public void logout() {
    userDatasource.logout();
  }
}
