package com.losextraditables.bu.pictures.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.pictures.domain.GetPicturesUseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PicturesPresenter extends BuPresenter<PicturesPresenter.View> {

  private final GetPicturesUseCase getPicturesUseCase;

  @Inject
  public PicturesPresenter(UseCaseHandler useCaseHandler, GetPicturesUseCase getPicturesUseCase) {
    super(useCaseHandler);
    this.getPicturesUseCase = getPicturesUseCase;
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
                //TODO show no pictures text
              }

              @Override public void onError(Throwable e) {
                getView().hideLoading();
                getView().showConnectionError();
              }

              @Override public void onNext(List<Picture> pictures) {
                //TODO MODEL List<InstagrammerModel> instagrammerModels = mapper.mapList(instagrammers);
                ArrayList<String> urls = new ArrayList<>(pictures.size());
                for (Picture picture : pictures) {
                  urls.add(picture.getUrl());
                }
                getView().hideLoading();
                getView().showSavedPictures(urls);
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

  public interface View extends BuPresenter.View {

    void showSavedPictures(ArrayList<String> urls);
  }
}
