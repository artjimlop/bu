package com.losextraditables.bu.pictures;

import android.support.annotation.RequiresPermission;
import com.losextraditables.bu.pictures.view.PictureActivity;
import com.losextraditables.bu.utils.WritePermissionManager;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        PictureActivity.class,
        WritePermissionManager.class
    }) public class PictureModule {
}
