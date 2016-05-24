package com.losextraditables.bu;

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.losextraditables.bu.base.view.error.BuErrorFactory;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.utils.SessionStore;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        BuApplication.class
    }) public class ApplicationModule {

  private final BuApplication application;

  public ApplicationModule(BuApplication application) {
    this.application = application;
  }

  @Provides
  public ErrorHandler providesErrorHandler(BuErrorFactory errorFactory) {
    return new ErrorHandler(errorFactory);
  }

  @Provides SessionManager providesSessionManager() {
    return new SessionStore(application);
  }
}
