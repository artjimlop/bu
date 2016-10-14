package com.losextraditables.bu.pictures.repository.datasource;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.bu.base.view.error.ConnectionError;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.utils.FirebaseService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ServicePictureDataSource implements PictureDataSource {

  private Boolean changeMade = false;
  private final FirebaseService firebaseService;

  @Inject public ServicePictureDataSource(FirebaseService firebaseService) {
    this.firebaseService = firebaseService;
  }

  @Override public Observable<Picture> getPictureFromScrap(final String url) {
    return Observable.just(getUrlFromScrap(url));
  }

  @Override public Observable<Void> savePicture(final Picture picture, final String uid) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        savePictureInFirebase(subscriber, uid, picture);
      }
    });
  }

  @Override
  public Observable<List<Picture>> getPictures(final String uid) {
    return Observable.create(new Observable.OnSubscribe<List<Picture>>() {
      @Override public void call(final Subscriber<? super List<Picture>> subscriber) {
        getPicturesFromFirebase(subscriber, uid);
      }
    });
  }

  @Override public Observable<Void> removePicture(final String uid, final String url) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        removePictureInFirebase(subscriber, uid, url);
      }
    });
  }

  private void getPicturesFromFirebase(final Subscriber<? super List<Picture>> subscriber,
      String uid) {
    final Firebase instagrammersReference = firebaseService.getPicturesReference(uid);
    instagrammersReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Picture>> t = new GenericTypeIndicator<List<Picture>>() {
        };
        List<Picture> instagrammers = dataSnapshot.getValue(t);
        subscriber.onNext(instagrammers);
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        Crashlytics.log(firebaseError.getMessage());
        subscriber.onError(new ConnectionError());
      }
    });
  }

  private void savePictureInFirebase(final Subscriber<? super Void> subscriber, String uid,
      final Picture picture) {
    final Firebase picturesReference = firebaseService.getPicturesReference(uid);
    picturesReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Picture>> t = new GenericTypeIndicator<List<Picture>>() {
        };
        List<Picture> pictures = dataSnapshot.getValue(t);
        if (!changeMade) {
          if (pictures != null) {
            pictures.add(picture);
            picturesReference.setValue(pictures);
          } else {
            pictures = Collections.singletonList(picture);
            picturesReference.setValue(pictures);
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
    saveDownloadedItem(picture);
  }

  private void removePictureInFirebase(final Subscriber<? super Void> subscriber, String uid,
      final String position) {
    final Firebase picturesReference = firebaseService.getPicturesReference(uid);
    picturesReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Picture>> t = new GenericTypeIndicator<List<Picture>>() {
        };
        List<Picture> pictures = dataSnapshot.getValue(t);
          if (pictures != null) {
            List<Picture> pics = new ArrayList<>();
            for (Picture picture : pictures) {
              if (!picture.getUrl().equals(position)) {
                pics.add(picture);
              }
            }
            picturesReference.setValue(pics);
          }
        subscriber.onCompleted();
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        Crashlytics.log(firebaseError.getMessage());
        subscriber.onError(new ConnectionError());
      }
    });
  }

  private Picture getUrlFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String possibleUrl = "";
      String username = "";
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("og:image")) {
          possibleUrl = String.valueOf(meta.attr("content"));
          break;
        }
        if (meta.attr("property").equals("og:description")) {
          username = String.valueOf(meta.attr("content"));
          break;
        }
      }
      Picture picture = new Picture();
      picture.setUrl(possibleUrl);
      picture.setOriginalUrl(url);
      picture.setUsername(username);
      return picture;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }

  public void saveDownloadedItem(final Picture picture) {
    changeMade = false;
    Firebase firebase = firebaseService.getBaseReference();
    final Firebase discover = firebase.child("discover");
    discover.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Picture>> t = new GenericTypeIndicator<List<Picture>>() {
        };
        List<Picture> pictures = dataSnapshot.getValue(t);
        List<Picture> modifiedPictures = new ArrayList<>();
        if (!changeMade) {
          if (pictures != null) {
            for (Picture parameters : pictures) {
              if (parameters.getOriginalUrl().equals(picture.getOriginalUrl())) {
                break;
              } else {
                modifiedPictures.add(parameters);
              }
            }
            discover.setValue(modifiedPictures);
          } else {
            pictures = Collections.singletonList(picture);
            discover.setValue(pictures);
          }
          changeMade = true;
        }
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        //TODO something
      }
    });
  }
}
