package com.losextraditables.bu.login.repository.datasource;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.karumi.rosie.repository.datasource.Identifiable;
import com.losextraditables.bu.login.domain.model.User;
import com.losextraditables.bu.utils.FirebaseService;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;

public class ServiceUserDataSource implements UserDatasource {

  private final FirebaseService firebaseService;

  @Inject public ServiceUserDataSource(FirebaseService firebaseService) {
    this.firebaseService = firebaseService;
  }

  @Override
  public Observable<Void> createUser(final String username, final String password) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override
      public void call(final Subscriber<? super Void> subscriber) {
        firebaseService.getFirebaseConnection().createUser(username, password,
            new Firebase.ValueResultHandler<Map<String, Object>>() {
              @Override
              public void onSuccess(Map<String, Object> result) {
                subscriber.onCompleted();
              }

              @Override
              public void onError(FirebaseError firebaseError) {
                Crashlytics.log(firebaseError.getMessage());
                subscriber.onError(new RuntimeException(firebaseError.getMessage()));
              }
            });
      }
    });
  }

  private void createUserEntity(final String username, String uid) {
    final Firebase userReference = firebaseService.createUserReference(uid);
    userReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() == null) {
          User newUser = new User();
          newUser.setUsername(username);
          newUser.setEmail(username);
          userReference.setValue(newUser);
        }
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        Crashlytics.log(firebaseError.getMessage());
      }
    });
  }

  @Override
  public Observable<String> login(final String username, final String password) {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override
      public void call(final Subscriber<? super String> subscriber) {
        authernticateUser(subscriber, username, password);
      }
    });
  }

  private void authernticateUser(final Subscriber<? super String> subscriber, final String username,
      String password) {
    firebaseService.getFirebaseConnection().authWithPassword(username, password,
        new Firebase.AuthResultHandler() {
          @Override
          public void onAuthenticated(AuthData authData) {
            createUserEntity(username, authData.getUid());
            subscriber.onNext(authData.getUid());
            subscriber.onCompleted();
          }

          @Override
          public void onAuthenticationError(FirebaseError firebaseError) {
            Crashlytics.log(firebaseError.getMessage());
            subscriber.onError(new RuntimeException(firebaseError.getMessage()));
          }
        });
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
