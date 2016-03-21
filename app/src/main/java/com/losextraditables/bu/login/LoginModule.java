package com.losextraditables.bu.login;

import com.losextraditables.bu.login.activity.LoginActivity;

import dagger.Module;

@Module(library = true,
        complete = false,
        injects = {
                LoginActivity.class
        })public class LoginModule {
}
