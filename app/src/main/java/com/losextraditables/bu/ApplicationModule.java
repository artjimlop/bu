package com.losextraditables.bu;

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.losextraditables.bu.base.view.error.BuErrorFactory;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        BuApplication.class
    }) public class ApplicationModule {

  @Provides
  public ErrorHandler providesErrorHandler(BuErrorFactory errorFactory) {
    return new ErrorHandler(errorFactory);
  }
}
