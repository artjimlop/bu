package com.losextraditables.bu.login.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.utils.ApplicationData;
import com.losextraditables.bu.utils.InstagramApp;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BuActivity {

    @Bind(R.id.btnConnect)
    Button login;

    @Inject InstagramApp instagramApp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new LoginModule());
    }

    @Override protected void onPreparePresenter() {
        super.onPreparePresenter();
    }

    @OnClick(R.id.btnConnect)
    public void onLoginPressed() {
        connectOrDisconnectUser();
    }

    private void connectOrDisconnectUser() {
        instagramApp.initialize(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        instagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                login.setText("Disconnect");
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        instagramApp.authorize();
    }

    private void displayInfoDialogView() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LoginActivity.this);
        alertDialog.setTitle("Profile Info");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertDialog.create().show();
    }
}
