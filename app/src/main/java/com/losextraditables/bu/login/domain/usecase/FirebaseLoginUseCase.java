package com.losextraditables.bu.login.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.login.repository.FirebaseUserRepository;

import javax.inject.Inject;

import rx.Observable;

public class FirebaseLoginUseCase extends RosieUseCase {

    private final FirebaseUserRepository repository;

    @Inject
    public FirebaseLoginUseCase(FirebaseUserRepository repository) {
        this.repository = repository;
    }

    @UseCase
    public void login(String username, String password) throws Exception {
        Observable<Void> login = repository.login(username, password);
        notifySuccess(login);
    }
}
