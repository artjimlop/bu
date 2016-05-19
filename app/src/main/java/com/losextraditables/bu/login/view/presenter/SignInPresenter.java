package com.losextraditables.bu.login.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.login.domain.usecase.CreateUserUseCase;
import com.losextraditables.bu.login.domain.usecase.LoginUseCase;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;

public class SignInPresenter extends BuPresenter<SignInPresenter.View> {

  private final LoginUseCase loginUseCase;
  private final CreateUserUseCase createUserUseCase;

  @Inject
  public SignInPresenter(UseCaseHandler useCaseHandler, LoginUseCase loginUseCase,
      CreateUserUseCase createUserUseCase) {
    super(useCaseHandler);
    this.loginUseCase = loginUseCase;
    this.createUserUseCase = createUserUseCase;
  }

  public void signInClicked(final String username, final String password) {
    getView().hideSignInButton();
    createUseCaseCall(loginUseCase).args(username, password).onSuccess(new OnSuccessCallback() {
      @Success
      public void onLogin(Observable<String> loginObservable) {
        loginObservable.subscribe(new Observer<String>() {
          @Override
          public void onCompleted() {
            getView().goToInstagrammersList();
          }

          @Override
          public void onError(Throwable e) {
            getView().showSignUp(username, password);
            getView().showSignInButton();
          }

          @Override
          public void onNext(String uid) {
            getView().saveUid(uid);
          }
        });
      }
    }).onError(new OnErrorCallback() {
      @Override
      public boolean onError(Error error) {
        getView().showConnectionError();
        getView().showSignInButton();
        return false;
      }
    }).execute();
  }

  public void signUp(final String username, final String password) {
    createUseCaseCall(createUserUseCase).args(username, password)
        .onSuccess(new OnSuccessCallback() {
          @Success
          public void onCreateUser(Observable<Void> createUserObservable) {
            createUserObservable.subscribe(new Observer<Void>() {
              @Override
              public void onCompleted() {
                signInClicked(username, password);
              }

              @Override
              public void onError(Throwable e) {
                getView().showGenericError();
                getView().showSignInButton();
              }

              @Override
              public void onNext(Void aVoid) {

              }
            });
          }
        })
        .onError(new OnErrorCallback() {
          @Override
          public boolean onError(Error error) {
            getView().showConnectionError();
            getView().showSignInButton();
            return false;
          }
        })
        .execute();
  }

  public interface View extends BuPresenter.View {

    void showSignUp(String username, String password);

    void goToInstagrammersList();

    void saveUid(String uid);

    void hideSignInButton();

    void showSignInButton();
  }
}
