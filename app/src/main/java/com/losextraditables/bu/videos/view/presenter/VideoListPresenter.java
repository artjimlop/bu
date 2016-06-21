package com.losextraditables.bu.videos.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.login.domain.usecase.RefreshAuthUseCase;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.domain.GetVideosUseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.model.mapper.VideoModelMapper;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoListPresenter extends BuPresenter<VideoListPresenter.View> {

  private final GetVideosUseCase getVideosUseCase;
  private final RefreshAuthUseCase refreshAuthUseCase;
  private final SessionManager sessionManager;
  private final VideoModelMapper mapper;

  @Inject public VideoListPresenter(UseCaseHandler useCaseHandler,
      GetVideosUseCase getVideosUseCase, RefreshAuthUseCase refreshAuthUseCase,
      SessionManager sessionManager, VideoModelMapper mapper) {
    super(useCaseHandler);
    this.getVideosUseCase = getVideosUseCase;
    this.refreshAuthUseCase = refreshAuthUseCase;
    this.sessionManager = sessionManager;
    this.mapper = mapper;
  }

  public void initialize() {
  }

  public void showVideos(String uid) {
    getView().showLoading();
    createUseCaseCall(getVideosUseCase).args(uid)
        .onSuccess(new OnSuccessCallback() {
          @Success
          public void onVideosLoaded(Observable<List<Video>> videoObservable) {
            videoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Video>>() {
                  @Override public void onCompleted() {
                    getView().hideLoading();
                  }

                  @Override public void onError(Throwable e) {
                    refreshAuth();
                    getView().showConnectionError();
                  }

                  @Override public void onNext(List<Video> videos) {
                    List<VideoModel> videoModels = mapper.mapList(videos);
                    showVideosInView(videoModels);
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

  private void showVideosInView(List<VideoModel> videoModels) {
    getView().showVideos(videoModels);
    getView().hideLoading();
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

  public interface View extends BuPresenter.View {
    void showVideos(List<VideoModel> videoModels);

    void goToVideo(VideoModel videoModel);
  }
}
