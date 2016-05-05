package com.losextraditables.bu.pictures.view.presenter;

import android.support.annotation.NonNull;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.pictures.domain.GetPictureUseCase;
import com.losextraditables.bu.pictures.domain.SavePictureUseCase;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PicturesPresenter extends BuPresenter<PicturesPresenter.View> {

  private final GetPictureUseCase getPictureUseCase;
  private final SavePictureUseCase savePictureUseCase;

  @Inject public PicturesPresenter(UseCaseHandler useCaseHandler,
      GetPictureUseCase getPictureUseCase, SavePictureUseCase savePictureUseCase) {
    super(useCaseHandler);
    this.getPictureUseCase = getPictureUseCase;
    this.savePictureUseCase = savePictureUseCase;
  }

  public void initialize() {
  }

  public void savePictureClicked() {
    getView().showSavePictureDialog();
  }

  public void savePicture(String url, @NonNull final String uid) {
    createUseCaseCall(getPictureUseCase).args(url, uid).onSuccess(new OnSuccessCallback() {
      @Success
      public void onPictureSaved(Observable<String> pictureObservable) {
        pictureObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<String>() {
          @Override public void onCompleted() {
          }

          @Override public void onError(Throwable e) {
            getView().showConnectionError();
          }

          @Override public void onNext(String pictureUrl) {
            addToUsersPicture(pictureUrl, uid);
          }
        });
      }
    }).onError(new OnErrorCallback() {
      @Override public boolean onError(Error error) {
        getView().showConnectionError();
        return false;
      }
    }).execute();
  }

  private void addToUsersPicture(final String url, String username) {
    createUseCaseCall(savePictureUseCase).args(url, username).onSuccess(new OnSuccessCallback() {
      @Success
      public void onPictureSaved(Observable<Void> savePicutreObservable) {
        savePicutreObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Void>() {
              @Override public void onCompleted() {
                getView().showPicture(url);
              }

              @Override public void onError(Throwable e) {

              }

              @Override public void onNext(Void aVoid) {

              }
            });
      }
    }).onError(new OnErrorCallback() {
      @Override public boolean onError(Error error) {
        return false;
      }
    }).execute();
  }

  public interface View extends BuPresenter.View {
    void showSavePictureDialog();

    void showPicture(String pictureUrl);
  }

}
