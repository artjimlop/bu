package com.losextraditables.bu.login.repository.datasource;

import com.karumi.rosie.repository.datasource.WriteableDataSource;
import rx.Observable;

public interface UserDatasource extends WriteableDataSource {

  Observable<Void> createUser(String username, String password);

  Observable<Void> login(String username, String password);
}
