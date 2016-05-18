package com.losextraditables.bu.pictures;

import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.WritePermissionManager;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        PictureActivity.class,
        WritePermissionManager.class,
        InstagrammersListActivity.class,
        PicturesActivity.class,
    }) public class PicturesModule {
}
