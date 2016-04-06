package com.losextraditables.bu.login.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.login.LoginModule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class FirebaseLoginActivity extends BuActivity{

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
        Firebase ref = new Firebase("https://buandroid.firebaseio.com");
        ref.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                goToInstagrammersList();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                showRegistrationDialog();
            }
        });
    }

    private void showRegistrationDialog() {
        System.out.println("credentials");
        System.out.println(email.getText().toString());
        System.out.println(password.getText().toString());
        new AlertDialog.Builder(this).setMessage("we need to register").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Firebase ref = new Firebase("https://buandroid.firebaseio.com");
                ref.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        goToInstagrammersList();
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        System.out.println(firebaseError.getMessage());
                    }
                });
            }
        }).create().show();
    }

    private void goToInstagrammersList() {
        Intent intent = new Intent(this, InstagrammersListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

