package com.losextraditables.bu.instagrammers.view.presenter;

import android.support.annotation.NonNull;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.GetFollowedInstagrammersUseCase;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.InstagrammerModelMapper;
import com.losextraditables.bu.pictures.domain.GetPictureUseCase;
import com.losextraditables.bu.pictures.domain.SavePictureUseCase;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

  private final GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase;
  private final GetPictureUseCase getPictureUseCase;
  private final SavePictureUseCase savePictureUseCase;
  private final InstagrammerModelMapper mapper;

  @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler,
      GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase,
      GetPictureUseCase getPictureUseCase, SavePictureUseCase savePictureUseCase,
      InstagrammerModelMapper mapper) {
    super(useCaseHandler);
    this.getFollowedInstagrammersUseCase = getFollowedInstagrammersUseCase;
    this.getPictureUseCase = getPictureUseCase;
    this.savePictureUseCase = savePictureUseCase;
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
                  }

                  @Override public void onError(Throwable e) {
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

  public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
    getView().goToInstagrammerDetail(instagrammerModel);
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
    void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels);

    void goToInstagrammerDetail(InstagrammerModel instagrammerModel);

    void showSavePictureDialog();

    void showPicture(String pictureUrl);
  }

  public interface ItemClickListener {
    void onItemClick(android.view.View view, InstagrammerModel instagrammerModel);
  }
}
