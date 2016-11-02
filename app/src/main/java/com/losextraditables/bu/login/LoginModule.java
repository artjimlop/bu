package com.losextraditables.bu.login;

import com.losextraditables.bu.login.repository.UserRepository;
import com.losextraditables.bu.login.repository.datasource.ServiceUserDataSource;
import com.losextraditables.bu.login.repository.datasource.UserDatasource;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.login.view.activity.SignInActivity;
import com.losextraditables.bu.utils.FirebaseService;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        LoginActivity.class,
        SignInActivity.class,
        UserRepository.class,
    }) public class LoginModule {

  @Provides
  public UserDatasource providesUserDataSource(FirebaseService firebaseService) {
    return new ServiceUserDataSource(firebaseService);
  }
}
