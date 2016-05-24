package com.losextraditables.bu.login.view.activity;

import android.content.Intent;
import butterknife.OnClick;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.login.LoginModule;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BuAppCompatActivity {

    @Override protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new LoginModule());
    }

    @Override protected void redirectToLogin() {
        /* no - op */
    }

    @Override protected void onPreparePresenter() {
        super.onPreparePresenter();
    }

    @OnClick(R.id.btnConnect) public void onLoginPressed() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
