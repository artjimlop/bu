package com.losextraditables.bu.instagrammers;

import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import com.losextraditables.bu.instagrammers.repository.datasource.InstagrammersApiDatasource;
import com.losextraditables.bu.instagrammers.repository.datasource.InstagrammersDatasource;
import com.losextraditables.bu.instagrammers.repository.service.InstagramServiceGenerator;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammerDetailActivity;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersFragment;
import com.losextraditables.bu.instagrammers.view.activity.SearchInstagrammersActivity;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.utils.FirebaseService;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        InstagrammersFragment.class, SearchInstagrammersActivity.class,
        InstagrammersApiDatasource.class, InstagrammersRepository.class,
        InstagrammerDetailActivity.class, MainTabbedActivity.class
    }) public class InstagrammersListModule {

  @Provides public InstagrammersDatasource providesInstagrammersDatasource(
      InstagramServiceGenerator generator, FirebaseService firebaseService) {
    return new InstagrammersApiDatasource(generator, firebaseService);
  }
}
