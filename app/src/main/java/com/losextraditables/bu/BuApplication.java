package com.losextraditables.bu;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.karumi.rosie.application.RosieApplication;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

public class BuApplication extends RosieApplication {
  private ObjectGraph fakeObjectGraph;

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    Firebase.setAndroidContext(this);
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
