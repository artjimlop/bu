package com.losextraditables.bu.login.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.login.LoginModule;
import com.losextraditables.bu.login.view.presenter.SignInPresenter;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.SessionManager;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class SignInActivity extends BuAppCompatActivity implements SignInPresenter.View {

    public static final int UI_ANIMATION_DURATION = 3000;

    @Inject @Presenter SignInPresenter presenter;
    @Inject SessionManager session;

    @Bind(R.id.email) AutoCompleteTextView email;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.email_login_button) Button signInButton;
    @Bind(R.id.container) LinearLayout container;

    @Override protected List<Object> getActivityScopeModules() {
        return Collections.singletonList((Object) new LoginModule());
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBarColor();
        setupBackgroundAnimation();
    }

    private void setupStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setupBackgroundAnimation() {
        Drawable backgrounds[] = new Drawable[2];
        Resources res = getResources();
        backgrounds[0] = res.getDrawable(R.color.colorPrimaryDark);
        backgrounds[1] = res.getDrawable(R.color.application_background);

        TransitionDrawable transitionDrawable = new TransitionDrawable(backgrounds);
        container.setBackground(transitionDrawable);
        transitionDrawable.startTransition(UI_ANIMATION_DURATION);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override protected void redirectToLogin() {

    }

    @OnClick(R.id.email_login_button) public void onSignInClick() {
        presenter.signInClicked(email.getText().toString(), password.getText().toString());
    }

    @Override public void showSignUp(final String username, final String password) {
        new AlertDialog.Builder(this).setMessage(R.string.sign_up_confirmation_message)
          .setPositiveButton(R.string.sign_up_confirmation_ok, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                  presenter.signUp(username, password);
              }
          })
          .setNegativeButton(R.string.sign_up_confirmation_no, null)
          .create()
          .show();
    }

    @Override public void goToInstagrammersList() {
        Intent intent = new Intent(this, PicturesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override public void hideLoading() {
        /* no-op */
    }

    @Override public void showLoading() {
        /* no-op */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override public void saveUid(String uid) {
      session.setUid(uid);
    }

    @Override public void hideSignInButton() {
        signInButton.setVisibility(View.GONE);
    }

    @Override public void showSignInButton() {
        signInButton.setVisibility(View.VISIBLE);
    }
}

