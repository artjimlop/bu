package com.losextraditables.bu.main;

import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.losextraditables.bu.videos.view.activity.VideoFragment;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        MainTabbedActivity.class, PicturesFragment.class, VideoFragment.class
    }) public class MainModule {
}
