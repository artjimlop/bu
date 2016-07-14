package com.losextraditables.bu.login.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.login.repository.UserRepository;
import javax.inject.Inject;
import rx.Observable;

public class RefreshAuthUseCase extends RosieUseCase {

  private final UserRepository repository;

  @Inject
  public RefreshAuthUseCase(UserRepository repository) {
    this.repository = repository;
  }

  @UseCase
  public void refreshAuth(String username, String password) throws Exception {
    repository.logout();
    Observable<String> login = repository.login(username, password);

    notifySuccess(login);
  }
}
