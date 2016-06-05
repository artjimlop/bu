package com.losextraditables.bu.videos.repository.datasource;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.bu.base.view.error.ConnectionError;
import com.losextraditables.bu.utils.FirebaseService;
import com.losextraditables.bu.videos.domain.model.Video;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;
import rx.Subscriber;

public class ServiceVideoDataSource implements VideoDataSource {

  private Boolean changeMade = false;
  private final FirebaseService firebaseService;

  @Inject public ServiceVideoDataSource(FirebaseService firebaseService) {
    this.firebaseService = firebaseService;
  }

  @Override public Observable<Video> getVideoFromScrap(final String url) {
    return Observable.just(getUrlFromScrap(url));
  }

  @Override public Observable<Void> saveVideo(final Video video, final String uid) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        saveVideoInFirebase(subscriber, uid, video);
      }
    });
  }

  @Override
  public Observable<List<Video>> getVideos(final String uid) {
    return Observable.create(new Observable.OnSubscribe<List<Video>>() {
      @Override public void call(final Subscriber<? super List<Video>> subscriber) {
        getPicturesFromFirebase(subscriber, uid);
      }
    });
  }

  private void getPicturesFromFirebase(final Subscriber<? super List<Video>> subscriber,
      String uid) {
    final Firebase reference = firebaseService.getVideosReference(uid);
    reference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Video>> t = new GenericTypeIndicator<List<Video>>() {
        };
        List<Video> videos = dataSnapshot.getValue(t);
        subscriber.onNext(videos);
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        Crashlytics.log(firebaseError.getMessage());
        subscriber.onError(new ConnectionError());
      }
    });
  }

  private void saveVideoInFirebase(final Subscriber<? super Void> subscriber, String uid,
      final Video video) {
    final Firebase picturesReference = firebaseService.getVideosReference(uid);
    picturesReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Video>> t = new GenericTypeIndicator<List<Video>>() {
        };
        List<Video> videos = dataSnapshot.getValue(t);
        if (!changeMade) {
          if (videos != null) {
            videos.add(video);
            picturesReference.setValue(videos);
          } else {
            videos = Collections.singletonList(video);
            picturesReference.setValue(videos);
          }
          changeMade = true;
        }
        subscriber.onCompleted();
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        Crashlytics.log(firebaseError.getMessage());
        subscriber.onError(new ConnectionError());
      }
    });
  }

  private Video getUrlFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String image = "";
      String title = "";
      String videoUrl = "";
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("og:image")) {
          image = String.valueOf(meta.attr("content"));
          break;
        }
        if (meta.attr("property").equals("og:title")) {
          title = String.valueOf(meta.attr("content"));
          break;
        }
        if (meta.attr("property").equals("og:video")) {
          videoUrl = String.valueOf(meta.attr("content"));
          break;
        }
      }
      Video video = new Video();
      video.setImage(image);
      video.setTitle(title);
      video.setUrl(videoUrl);
      return video;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }
}
