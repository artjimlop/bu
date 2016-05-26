package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.GetFollowedInstagrammersUseCase;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.InstagrammerModelMapper;
import com.losextraditables.bu.login.domain.usecase.RefreshAuthUseCase;
import com.losextraditables.bu.utils.SessionManager;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

  private final GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase;
  private final RefreshAuthUseCase refreshAuthUseCase;
  private final SessionManager sessionManager;
  private final InstagrammerModelMapper mapper;

  @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler,
      GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase,
      RefreshAuthUseCase refreshAuthUseCase, SessionManager sessionManager, InstagrammerModelMapper mapper) {
    super(useCaseHandler);
    this.getFollowedInstagrammersUseCase = getFollowedInstagrammersUseCase;
    this.refreshAuthUseCase = refreshAuthUseCase;
    this.sessionManager = sessionManager;
    this.mapper = mapper;
  }

  public void initialize() {
  }

  public void showInstagrammers(String uid) {
    getView().showLoading();
    createUseCaseCall(getFollowedInstagrammersUseCase).args(uid)
        .onSuccess(new OnSuccessCallback() {
          @Success
          public void onInstagrammersLoaded(Observable<List<Instagrammer>> instagrammersObservable) {
            instagrammersObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Instagrammer>>() {
                  @Override public void onCompleted() {
                    getView().hideLoading();
                    //TODO show no instagrammers text on view
                  }

                  @Override public void onError(Throwable e) {
                    refreshAuth();
                    getView().showConnectionError();
                  }

                  @Override public void onNext(List<Instagrammer> instagrammers) {
                    List<InstagrammerModel> instagrammerModels = mapper.mapList(instagrammers);
                    getView().showMockedInstagrammers(instagrammerModels);
                    getView().hideLoading();
                  }
                });
          }
        })
        .onError(new OnErrorCallback() {
          @Override public boolean onError(Error error) {
            getView().hideLoading();
            return false;
          }
        })
        .execute();
  }

  public void refreshAuth() {
    String email = sessionManager.getEmail();
    String password = sessionManager.getPassword();
    createUseCaseCall(refreshAuthUseCase).args(email, password).onSuccess(new OnSuccessCallback() {
      @Success public void onAuthRefreshed(Observable<String> observable) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<String>() {
              @Override public void onCompleted() {

              }

              @Override public void onError(Throwable e) {

              }

              @Override public void onNext(String uid) {
                sessionManager.setUid(uid);
              }
            });
      }
    }).onError(new OnErrorCallback() {
      @Override public boolean onError(Error error) {
        getView().hideLoading();
        return false;
      }
    }).execute();
  }

  public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
    getView().goToInstagrammerDetail(instagrammerModel);
  }

  public interface View extends BuPresenter.View {
    void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels);

    void goToInstagrammerDetail(InstagrammerModel instagrammerModel);

  }

  public interface ItemClickListener {
    void onItemClick(android.view.View view, InstagrammerModel instagrammerModel);
  }
}
