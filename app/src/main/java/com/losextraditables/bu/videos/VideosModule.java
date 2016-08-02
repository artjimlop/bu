package com.losextraditables.bu.videos;

import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.utils.FirebaseService;
import com.losextraditables.bu.videos.repository.VideoRepository;
import com.losextraditables.bu.videos.repository.datasource.ServiceVideoDataSource;
import com.losextraditables.bu.videos.repository.datasource.VideoDataSource;
import com.losextraditables.bu.videos.view.activity.VideoFragment;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        ServiceVideoDataSource.class,
        VideoRepository.class, VideoFragment.class, MainTabbedActivity.class
    })public class VideosModule {
  @Provides
  public VideoDataSource providesVideoDataSource(FirebaseService firebaseService) {
    return new ServiceVideoDataSource(firebaseService);
  }
}
