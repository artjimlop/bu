package com.losextraditables.bu.pictures;

import com.losextraditables.bu.instagrammers.view.activity.InstagrammersFragment;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.pictures.repository.PictureRepository;
import com.losextraditables.bu.pictures.repository.datasource.PictureDataSource;
import com.losextraditables.bu.pictures.repository.datasource.ServicePictureDataSource;
import com.losextraditables.bu.pictures.view.activity.GalleryActivity;
import com.losextraditables.bu.pictures.view.activity.LatestFragment;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.losextraditables.bu.utils.FirebaseService;
import com.losextraditables.bu.utils.WritePermissionManager;
import dagger.Module;
import dagger.Provides;

@Module(library = true,
    complete = false,
    injects = {
        PictureActivity.class,
        WritePermissionManager.class,
        InstagrammersFragment.class,
        PicturesFragment.class,
        LatestFragment.class,
        ServicePictureDataSource.class,
        PictureRepository.class, GalleryActivity.class, MainTabbedActivity.class
    }) public class PicturesModule {

    @Provides
    public PictureDataSource providesPictureDataSource(FirebaseService firebaseService) {
        return new ServicePictureDataSource(firebaseService);
    }
}
