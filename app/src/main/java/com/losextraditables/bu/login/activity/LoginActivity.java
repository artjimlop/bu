package com.losextraditables.bu.login.activity;

import android.content.Intent;
import android.widget.Toast;

import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.utils.ApplicationData;
import com.losextraditables.bu.utils.InstagramLogin;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

public class LoginActivity extends BuActivity {

    @Inject
    InstagramLogin instagramLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new LoginModule());
    }

    @Override
    protected void redirectToLogin() {
        /* no - op */
    }

    @Override protected void onPreparePresenter() {
        super.onPreparePresenter();
    }

    @OnClick(R.id.btnConnect)
    public void onLoginPressed() {
        connectOrDisconnectUser();
    }

    private void connectOrDisconnectUser() {
        instagramLogin.initialize(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        instagramLogin.setListener(new InstagramLogin.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                goToFirebaseLogin();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        instagramLogin.authorize();
    }

    private void goToFirebaseLogin() {
        Intent intent = new Intent(this, FirebaseLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
