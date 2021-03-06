package com.losextraditables.bu.videos.view.presenter;

import android.support.annotation.NonNull;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.login.domain.usecase.RefreshAuthUseCase;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.domain.GetVideoUseCase;
import com.losextraditables.bu.videos.domain.GetVideosUseCase;
import com.losextraditables.bu.videos.domain.RemoveVideoUseCase;
import com.losextraditables.bu.videos.domain.SaveVideoUseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.model.mapper.VideoModelMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoListPresenter extends BuPresenter<VideoListPresenter.View> {

  private final GetVideosUseCase getVideosUseCase;
  private final SaveVideoUseCase saveVideoUseCase;
  private final GetVideoUseCase getVideoUseCase;
  private final RemoveVideoUseCase removeVideoUseCase;
  private final RefreshAuthUseCase refreshAuthUseCase;
  private final SessionManager sessionManager;
  private final VideoModelMapper mapper;
  private List<VideoModel> videoModels;

  @Inject public VideoListPresenter(UseCaseHandler useCaseHandler,
      GetVideosUseCase getVideosUseCase, SaveVideoUseCase saveVideoUseCase,
      GetVideoUseCase getVideoUseCase, RemoveVideoUseCase removeVideoUseCase,
      RefreshAuthUseCase refreshAuthUseCase,
      SessionManager sessionManager, VideoModelMapper mapper) {
    super(useCaseHandler);
    this.getVideosUseCase = getVideosUseCase;
    this.saveVideoUseCase = saveVideoUseCase;
    this.getVideoUseCase = getVideoUseCase;
    this.removeVideoUseCase = removeVideoUseCase;
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
                      Collections.reverse(videos);
                      videoModels = mapper.mapList(videos);
                      if (!videoModels.isEmpty()) {
                      getView().showVideos(videoModels);
                      } else {
                          getView().showRetry();
                      }
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

  public void saveVideo(final String url, @NonNull final String uid) {
    createUseCaseCall(getVideoUseCase).args(url).onSuccess(new OnSuccessCallback() {
      @Success public void onVideoSaved(Observable<Video> videoObservable) {
        videoObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Video>() {
              @Override public void onCompleted() {
              }

              @Override public void onError(Throwable e) {
                getView().showConnectionError();
              }

              @Override public void onNext(Video video) {
                addToUserVideos(video, uid);
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

  public void addToUserVideos(Video video, String uid) {
    createUseCaseCall(saveVideoUseCase).args(video, uid).onSuccess(new OnSuccessCallback() {
      @Success public void onVideoSaved(Observable<Void> savePicutreObservable) {
        savePicutreObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Void>() {
              @Override public void onCompleted() {
                getView().refresh();
              }

              @Override public void onError(Throwable e) {
                /* no-op */
              }

              @Override public void onNext(Void aVoid) {
                /* no-op */
              }
            });
      }
    }).onError(new OnErrorCallback() {
      @Override public boolean onError(Error error) {
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

  public void onAddVideoClick() {
    getView().showAddVideoDialog();
  }

  public void removeVideo(String uid, final String url) {
    createUseCaseCall(removeVideoUseCase).args(uid, url)
        .onSuccess(new OnSuccessCallback() {
          @Success
          public void onVideosLoaded(Observable<List<Video>> videoObservable) {
            videoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Video>>() {
                  @Override public void onCompleted() {
                    List<VideoModel> videos = new ArrayList<>();
                    for (VideoModel video : videoModels) {
                      if (!video.getUrl().equals(url)) {
                        videos.add(video);
                      }
                    }
                    getView().showVideos(videos);
                    videoModels = videos;
                  }

                  @Override public void onError(Throwable e) {
                  }

                  @Override public void onNext(List<Video> videos) {
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

  public interface View extends BuPresenter.View {
    void showVideos(List<VideoModel> videoModels);

    void showAddVideoDialog();

    void refresh();

      void showRetry();
  }
}
