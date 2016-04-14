package com.losextraditables.bu.login.repository.datasource;

import android.support.annotation.NonNull;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.karumi.rosie.repository.datasource.Identifiable;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;

public class FirebaseUserDataSource implements UserDatasource {

  public static final String FIREBASE_URL = "https://buandroid.firebaseio.com";

  @Inject public FirebaseUserDataSource() {
  }

  @Override
  public Observable<Void> createUser(final String username, final String password) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override
      public void call(final Subscriber<? super Void> subscriber) {
        getFirebaseConnection().createUser(username, password,
            new Firebase.ValueResultHandler<Map<String, Object>>() {
              @Override
              public void onSuccess(Map<String, Object> result) {
                subscriber.onCompleted();
              }

              @Override
              public void onError(FirebaseError firebaseError) {
                subscriber.onError(new RuntimeException(firebaseError.getMessage()));
              }
            });
      }
    });
  }

  @Override
  public Observable<Void> login(final String username, final String password) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override
      public void call(final Subscriber<? super Void> subscriber) {
        getFirebaseConnection().authWithPassword(username, password,
            new Firebase.AuthResultHandler() {
              @Override
              public void onAuthenticated(AuthData authData) {
                subscriber.onCompleted();
              }

              @Override
              public void onAuthenticationError(FirebaseError firebaseError) {
                subscriber.onError(new RuntimeException(firebaseError.getMessage()));
              }
            });
      }
    });
  }

  @NonNull
  private Firebase getFirebaseConnection() {
    return new Firebase(FIREBASE_URL);
  }

  @Override
  public Identifiable addOrUpdate(Identifiable value) throws Exception {
    return null;
  }

  @Override
  public Collection addOrUpdateAll(Collection values) throws Exception {
    return null;
  }

  @Override
  public void deleteByKey(Object key) throws Exception {

  }

  @Override
  public void deleteAll() throws Exception {

  }
}
