package com.losextraditables.bu.instagrammers;

import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.instagrammers.view.activity.SearchInstagrammersActivity;
import dagger.Module;

@Module(library = true,
    complete = false,
    injects = {
        InstagrammersListActivity.class,
        SearchInstagrammersActivity.class
    }) public class InstagrammersListModule {
}
