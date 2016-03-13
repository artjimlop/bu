package com.losextraditables.bu.instagrammers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.instagrammers.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.login.activity.LoginActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class InstagrammersListActivity extends BuActivity implements InstagrammersListPresenter.View {

    @Bind(R.id.contant_main_text)
    TextView contentText;

    @Inject
    @Presenter
    InstagrammersListPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_instagrammers;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new InstagrammersListModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override protected void onPreparePresenter() {
        super.onPreparePresenter();
        presenter.initialize();
    }

    @Override
    public void showHelloWorld(String hello) {
        contentText.setText(hello);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }
}
