package com.losextraditables.bu.base.view;

import android.content.Context;
import com.karumi.rosie.daggerutils.ForApplication;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.utils.SessionStore;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(library = true,
    complete = false,
    injects = {
        BuActivity.class,
    }) public class BaseModule {

  private final Context context;

  public BaseModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton @ForApplication Context provideContext() {
    return context;
  }

  @Provides SessionManager providesSessionManager() {
    return new SessionStore(context);
  }
}
