package com.losextraditables.bu.base.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.karumi.rosie.view.RosieActivity;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.BaseModule;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.login.InstagramSession;
import com.losextraditables.bu.login.activity.LoginActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public abstract class BuActivity extends RosieActivity {

    @Inject
    InstagramSession instagramSession;

    @Override protected abstract int getLayoutId();

    public void showGenericError() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
    }

    public void showConnectionError() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!instagramSession.hasSession(this)) {
            //redirectToLogin();
        }
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new BaseModule());
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}


