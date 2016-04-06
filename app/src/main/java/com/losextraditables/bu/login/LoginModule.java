package com.losextraditables.bu.login;

import com.losextraditables.bu.login.view.activity.FirebaseLoginActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;

import dagger.Module;

@Module(library = true,
        complete = false,
        injects = {
                LoginActivity.class,
                FirebaseLoginActivity.class
        })public class LoginModule {
}
