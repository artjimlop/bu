package com.losextraditables.bu.utils;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import javax.inject.Inject;

public class AuthenticationHandler {

  @Inject SessionManager session;

  @Inject public AuthenticationHandler() {
  }

  public void authenticationRefresh(final Context context) {
    final Firebase ref = new Firebase("https://buandroid.firebaseio.com");
    ref.addAuthStateListener(new Firebase.AuthStateListener() {
      @Override
      public void onAuthStateChanged(AuthData authData) {
        if (authData == null) {
          final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
              // Authenticated successfully with payload authData
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
              Crashlytics.log(firebaseError.getMessage());
            }
          };
          String uid = session.getUid();
          if (uid != null) {
            ref.authWithCustomToken(uid, authResultHandler);
          }
        }
      }
    });
  }
}
