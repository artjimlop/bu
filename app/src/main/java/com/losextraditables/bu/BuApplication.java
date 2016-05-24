package com.losextraditables.bu;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.karumi.rosie.application.RosieApplication;
import com.losextraditables.bu.utils.InstagramSession;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class BuApplication extends RosieApplication {

  @Inject InstagramSession session;

  private ObjectGraph fakeObjectGraph;

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    Firebase.setAndroidContext(this);

    final Firebase ref = new Firebase("https://buandroid.firebaseio.com");

    ref.addAuthStateListener(new Firebase.AuthStateListener() {
      @Override
      public void onAuthStateChanged(AuthData authData) {
        if (authData == null) {
          Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
              // Authenticated successfully with payload authData
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
              Crashlytics.log(firebaseError.getMessage());
            }
          };
          ref.authWithCustomToken(session.getUid(getApplicationContext()), authResultHandler);
        }
      }
    });
  }

  @Override protected List<Object> getApplicationModules() {
    return Arrays.asList((Object) new ApplicationModule());
  }

  public void replaceGraph(ObjectGraph objectGraph) {
    this.fakeObjectGraph = objectGraph;
  }

  @Override public ObjectGraph plusGraph(List<Object> activityScopeModules) {
    ObjectGraph newObjectGraph;
    if (fakeObjectGraph == null) {
      newObjectGraph = super.plusGraph(activityScopeModules);
    } else {
      newObjectGraph = fakeObjectGraph.plus(activityScopeModules.toArray());
    }
    return newObjectGraph;
  }

  public void resetFakeGraph() {
    fakeObjectGraph = null;
  }
}
