package com.losextraditables.bu.login;

import com.losextraditables.bu.login.view.activity.SignInActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;

import dagger.Module;

@Module(library = true,
        complete = false,
        injects = {
                LoginActivity.class,
                SignInActivity.class
        })public class LoginModule {
}
