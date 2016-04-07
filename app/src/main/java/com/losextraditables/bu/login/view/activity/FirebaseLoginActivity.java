package com.losextraditables.bu.login.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.login.view.presenter.FirebaseLoginPresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class FirebaseLoginActivity extends BuActivity implements FirebaseLoginPresenter.View{

    @Inject @Presenter
    FirebaseLoginPresenter presenter;

    @Bind(R.id.email)
    AutoCompleteTextView email;
    @Bind(R.id.password)
    EditText password;

    @Override protected List<Object> getActivityScopeModules() {
        return Collections.singletonList((Object) new LoginModule());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_firebase_login;
    }

    @Override
    protected void redirectToLogin() {

    }

    @OnClick(R.id.email_login_button) public void onSignInClick() {
        presenter.signInClicked(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void showSignUp(final String username, final String password) {
        new AlertDialog.Builder(this).setMessage("we need to register").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.signUp(username, password);
            }
        }).create().show();
    }

    @Override
    public void goToInstagrammersList() {
        Intent intent = new Intent(this, InstagrammersListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }
}

