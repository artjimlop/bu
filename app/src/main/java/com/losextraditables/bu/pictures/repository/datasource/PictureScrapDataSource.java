package com.losextraditables.bu.pictures.repository.datasource;

import android.support.annotation.NonNull;
import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.bu.base.view.error.ConnectionError;
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

  @Override public Observable<String> getPictureFromScrap(final String url) {
    return Observable.just(getUrlFromScrap(url));
  }

  @Override public Observable<Void> savePicture(final String url, final String uid) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        final Firebase picturesReference = new Firebase("https://buandroid.firebaseio.com/users").child(uid).child("pictures");
        picturesReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("FIREBASE", "listener");
            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
            List<String> pictures = dataSnapshot.getValue(t);
            if (!changeMade) {
              if (pictures != null) {
                pictures.add(url);
                picturesReference.setValue(pictures);
              } else {
                pictures = Collections.singletonList(url);
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

  private String getUrlFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String possibleUrl = "";
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("og:image")) {
          possibleUrl = String.valueOf(meta.attr("content"));
          break;
        }
      }
      return possibleUrl;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }
}
