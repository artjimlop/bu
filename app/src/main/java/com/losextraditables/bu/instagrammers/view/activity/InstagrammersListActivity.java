package com.losextraditables.bu.instagrammers.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.View;

import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.instagrammers.view.adapter.InstagrammersAdapter;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.login.activity.LoginActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class InstagrammersListActivity extends BuActivity implements InstagrammersListPresenter.View {

    @Bind(R.id.instagrammers_list)
    RecyclerView instagrammersList;

    @Inject
    @Presenter
    InstagrammersListPresenter presenter;

    private InstagrammersAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private View sharedImage;

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
        setupWindowAnimations();
        adapter = new InstagrammersAdapter(this, new InstagrammersListPresenter.ItemClickListener() {
            @Override
            public void onItemClick(View view, InstagrammerModel instagrammerModel) {
                sharedImage = view.findViewById(R.id.hover_instagrammer_avatar);
                presenter.goToInstagrammerDetail(instagrammerModel);
            }
        });
        instagrammersList.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        instagrammersList.setLayoutManager(linearLayoutManager);
        presenter.showMockedInstagrammers();
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
    public void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels) {
        adapter.setUsers(instagrammerModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
        InstagrammerDetailActivity.init(this, sharedImage, instagrammerModel);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        getWindow().setReenterTransition(new Explode());
        getWindow().setExitTransition(new Explode().setDuration(500));
    }

}
