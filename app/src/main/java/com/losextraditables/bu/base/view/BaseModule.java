package com.losextraditables.bu.base.view;

import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.utils.InstagramSession;
import com.losextraditables.bu.login.Session;

import dagger.Module;
import dagger.Provides;

@Module(library = true,
        complete = false,
        injects = {
                BuActivity.class
        })public class BaseModule {

        @Provides
        public Session providesInstagramSession(InstagramSession instagramSession) {
                return instagramSession;
        }
}
