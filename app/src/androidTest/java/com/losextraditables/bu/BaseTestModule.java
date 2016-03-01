package com.losextraditables.bu;

import android.content.Context;

import com.karumi.rosie.daggerutils.ForActivity;

import dagger.Module;
import dagger.Provides;

@Module(overrides = true, complete = false, library = true, includes = {
        ApplicationModule.class
}) public class BaseTestModule {

    private final Context context;

    public BaseTestModule(Context context) {
        this.context = context;
    }

    @Provides
    @ForActivity
    Context provideActivityContext() {
        return context;
    }
}
