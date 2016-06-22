package com.losextraditables.bu.videos.view.presenter;

import android.support.annotation.NonNull;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.login.domain.usecase.RefreshAuthUseCase;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.domain.GetVideoUseCase;
import com.losextraditables.bu.videos.domain.GetVideosUseCase;
import com.losextraditables.bu.videos.domain.SaveVideoUseCase;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.model.mapper.VideoModelMapper;
import java.util.ArrayList;
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
  private final RefreshAuthUseCase refreshAuthUseCase;
  private final SessionManager sessionManager;
  private final VideoModelMapper mapper;

  @Inject public VideoListPresenter(UseCaseHandler useCaseHandler,
      GetVideosUseCase getVideosUseCase, SaveVideoUseCase saveVideoUseCase,
      GetVideoUseCase getVideoUseCase, RefreshAuthUseCase refreshAuthUseCase,
      SessionManager sessionManager, VideoModelMapper mapper) {
    super(useCaseHandler);
    this.getVideosUseCase = getVideosUseCase;
    this.saveVideoUseCase = saveVideoUseCase;
    this.getVideoUseCase = getVideoUseCase;
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
                    getView().showVideos(videoModels);
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
   /* ArrayList<VideoModel> videoModels1 = new ArrayList<>();

    for(int i= 0; i<10; i++) {
      VideoModel videoModel = new VideoModel();
      videoModel.setTitle("video " + i);
      videoModel.setUrl("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
      videoModel.setImage("http://wiizom.click/wp-content/uploads/2016/01/2015-Chloe-Grace-Moretz-66.png");
      videoModels1.add(videoModel);
    }
    getView().showVideos(videoModels1);*/
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

  public interface View extends BuPresenter.View {
    void showVideos(List<VideoModel> videoModels);

    void showAddVideoDialog();

    void refresh();
  }
}
