package com.losextraditables.bu.login.repository.datasource;

import android.support.annotation.NonNull;
import android.util.Log;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.karumi.rosie.repository.datasource.Identifiable;
import com.losextraditables.bu.login.domain.model.User;
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

  private void createUserEntity(final String username, String uid) {
    final Firebase userReference = new Firebase("https://buandroid.firebaseio.com/users").child(uid);
    userReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<User> user = new GenericTypeIndicator<User>() {
        };
        if (dataSnapshot.getValue() == null) {
          User newUser = new User();
          newUser.setUsername(username);
          newUser.setEmail(username);
          userReference.setValue(newUser);
        }
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        //TODO LOG
        Log.e("FIREBASE", firebaseError.getMessage());
      }
    });
  }

  @Override
  public Observable<String> login(final String username, final String password) {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override
      public void call(final Subscriber<? super String> subscriber) {
        getFirebaseConnection().authWithPassword(username, password,
            new Firebase.AuthResultHandler() {
              @Override
              public void onAuthenticated(AuthData authData) {
                createUserEntity(username, authData.getUid());
                subscriber.onNext(authData.getUid());
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
