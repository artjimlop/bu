package com.losextraditables.bu.main;

import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        MainTabbedActivity.class, PicturesFragment.class
    }) public class MainModule {
}
