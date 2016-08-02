package com.losextraditables.bu;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.karumi.rosie.application.RosieApplication;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.main.MainModule;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.utils.AuthenticationHandler;
import com.losextraditables.bu.videos.VideosModule;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class BuApplication extends RosieApplication {

  @Inject AuthenticationHandler authenticationHandler;

  private ObjectGraph fakeObjectGraph;

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    Firebase.setAndroidContext(this);
    authenticationHandler.authenticationRefresh(getApplicationContext());
  }

  @Override protected List<Object> getApplicationModules() {
    return Arrays.asList(new ApplicationModule(this), new InstagrammersListModule(),
        new LoginModule(), new PicturesModule(), new MainModule(), new VideosModule());
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

  public ObjectGraph getFakeObjectGraph() {
    return fakeObjectGraph;
  }

  public static BuApplication get(Context context) {
    return (BuApplication) context.getApplicationContext();
  }
}
