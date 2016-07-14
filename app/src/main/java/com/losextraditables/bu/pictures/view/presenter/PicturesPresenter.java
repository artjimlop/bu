package com.losextraditables.bu.pictures.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.login.domain.usecase.RefreshAuthUseCase;
import com.losextraditables.bu.pictures.domain.GetPicturesUseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.domain.model.mapper.PictureModelMapper;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.losextraditables.bu.utils.SessionManager;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PicturesPresenter extends BuPresenter<PicturesPresenter.View> {

  private final GetPicturesUseCase getPicturesUseCase;
  private final RefreshAuthUseCase refreshAuthUseCase;
  private final SessionManager sessionManager;
  private final PictureModelMapper pictureModelMapper;

  @Inject
  public PicturesPresenter(UseCaseHandler useCaseHandler, GetPicturesUseCase getPicturesUseCase,
      RefreshAuthUseCase refreshAuthUseCase, SessionManager sessionManager,
      PictureModelMapper pictureModelMapper) {
    super(useCaseHandler);
    this.getPicturesUseCase = getPicturesUseCase;
    this.refreshAuthUseCase = refreshAuthUseCase;
    this.sessionManager = sessionManager;
    this.pictureModelMapper = pictureModelMapper;
  }

  public void initialize() {
  }

  public void loadSavedPictures(String uid) {
    getView().showLoading();
    createUseCaseCall(getPicturesUseCase).args(uid).onSuccess(new OnSuccessCallback() {
      @Success public void onInstagrammersLoaded(Observable<List<Picture>> observable) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Picture>>() {
              @Override public void onCompleted() {
                getView().hideLoading();
              }

              @Override public void onError(Throwable e) {
                getView().hideLoading();
                getView().showConnectionError();
                refreshAuth();
              }

              @Override public void onNext(List<Picture> pictures) {
                List<PictureModel> pictureModels = pictureModelMapper.listMap(pictures);
                getView().hideLoading();
                getView().showSavedPictures(pictureModels);
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

  public void onAddPictureClick() {
    getView().showSavePictureDialog();
  }

  public interface View extends BuPresenter.View {

    void showSavedPictures(List<PictureModel> pictures);

    void showSavePictureDialog();
  }
}
