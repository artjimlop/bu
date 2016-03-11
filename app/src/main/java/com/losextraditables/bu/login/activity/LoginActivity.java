package com.losextraditables.bu.login.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.utils.InstagramSession;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.utils.ApplicationData;
import com.losextraditables.bu.utils.InstagramLogin;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BuActivity {

    @Bind(R.id.btnConnect)
    Button login;

    @Inject
    InstagramLogin instagramLogin;
    @Inject
    InstagramSession instagramSession;

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
                login.setText("Disconnect");
                Log.d("SESION: ", instagramSession.getAccessToken(getBaseContext()));
                Log.d("SESION: ", instagramSession.getId(getBaseContext()));
                Log.d("SESION: ", instagramSession.getUsername(getBaseContext()));
                Log.d("SESION: ", instagramSession.getName(getBaseContext()));
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        instagramLogin.authorize();
    }
}
