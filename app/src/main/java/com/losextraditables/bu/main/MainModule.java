package com.losextraditables.bu.main;

import com.losextraditables.bu.instagrammers.view.activity.InstagrammersFragment;
import com.losextraditables.bu.pictures.view.activity.LatestFragment;
import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.losextraditables.bu.videos.view.activity.VideoFragment;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        MainTabbedActivity.class, PicturesFragment.class, VideoFragment.class,
        InstagrammersFragment.class,
        LatestFragment.class
    }) public class MainModule {
}
