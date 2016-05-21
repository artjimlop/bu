package com.losextraditables.bu.login.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.login.repository.UserRepository;
import javax.inject.Inject;
import rx.Observable;

public class CreateUserUseCase extends RosieUseCase {

  private final UserRepository repository;

  @Inject public CreateUserUseCase(UserRepository repository) {
    this.repository = repository;
  }

  @UseCase
  public void createUser(String username, String password) throws Exception {
    Observable<Void> createUserObservable = repository.createUser(username, password);
    notifySuccess(createUserObservable);
  }
}
