package com.losextraditables.bu.pictures.repository.datasource;

import android.support.annotation.NonNull;
import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.bu.base.view.error.ConnectionError;
import com.losextraditables.bu.pictures.domain.model.Picture;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;
import rx.Subscriber;

public class PictureScrapDataSource implements PictureDataSource {

  public static final String FIREBASE_URL = "https://buandroid.firebaseio.com";

  private Boolean changeMade = false;

  @Inject public PictureScrapDataSource() {
  }

  @Override public Observable<Picture> getPictureFromScrap(final String url) {
    return Observable.just(getUrlFromScrap(url));
  }

  @Override public Observable<Void> savePicture(final Picture picture, final String uid) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        final Firebase picturesReference =
            new Firebase("https://buandroid.firebaseio.com/users").child(uid).child("pictures");
        picturesReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("FIREBASE", "listener");
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
            //TODO LOG
            Log.e("FIREBASE", firebaseError.getMessage());
            subscriber.onError(new ConnectionError());
          }
        });
      }
    });
  }

  @NonNull
  private Firebase getFirebaseConnection() {
    return new Firebase(FIREBASE_URL);
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

  @Override
  public Observable<List<Picture>> getPictures(final String uid) {
    return Observable.create(new Observable.OnSubscribe<List<Picture>>() {
      @Override public void call(final Subscriber<? super List<Picture>> subscriber) {
        final Firebase instagrammersReference =
            new Firebase("https://buandroid.firebaseio.com/users").child(uid).child("pictures");
        instagrammersReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Picture>> t = new GenericTypeIndicator<List<Picture>>() {
            };
            List<Picture> instagrammers = dataSnapshot.getValue(t);
            subscriber.onNext(instagrammers);
          }

          @Override public void onCancelled(FirebaseError firebaseError) {
            //TODO LOG
            Log.e("FIREBASE", firebaseError.getMessage());
            subscriber.onError(new ConnectionError());
          }
        });
      }
    });
  }
}
