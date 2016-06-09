package com.losextraditables.bu.videos.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.bottombar.view.BottomBarPresenter;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.VideosModule;
import com.losextraditables.bu.videos.view.activity.adapter.VideoAdapter;
import com.losextraditables.bu.videos.view.activity.adapter.VideoItemClickListener;
import com.losextraditables.bu.videos.view.activity.model.VideoModel;
import com.losextraditables.bu.videos.view.activity.presenter.VideosPresenter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideosActivity extends BuAppCompatActivity
        implements BottomBarPresenter.View, VideosPresenter.View {

    @Bind(R.id.recycler_view)
    RecyclerView videosRecycler;
    @Bind(R.id.video)
    VideoView videoView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Inject
    SessionManager session;

    @Inject
    @Presenter
    BottomBarPresenter bottomBarPresenter;

    @Inject
    @Presenter
    VideosPresenter videosPresenter;

    private BottomBar bottomBar;
    private VideoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean justInitialized = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_videos;
    }

    @Override protected List<Object> getActivityScopeModules() {
        return Arrays.asList((Object) new VideosModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupVideosList();
        setupBottomBar(savedInstanceState, this);
    }

    private void setupVideosList() {
        adapter = new VideoAdapter(this, new VideoItemClickListener() {
            @Override
            public void onItemClick(VideoModel videoModel) {
                playVideo(videoModel);
            }
        });
        videosRecycler.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        videosRecycler.setLayoutManager(linearLayoutManager);
        //videosPresenter.showVideos(session.getUid());
        videosPresenter.prueba();
    }

    private void setupBottomBar(Bundle savedInstanceState, final Context context) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.noNavBarGoodness();
        bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottom_save_picture) {
                    bottomBarPresenter.savePictureClicked();
                } else if (menuItemId == R.id.bottom_save_instagrammers) {
                    bottomBarPresenter.saveInstagrammerClicked();
                } else if (menuItemId == R.id.bottom_pictures) {
                    if (!justInitialized) {
                        startActivity(new Intent(context, PicturesActivity.class));
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        finish();
                    }
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottom_save_picture) {
                    bottomBarPresenter.savePictureClicked();
                } else if (menuItemId == R.id.bottom_save_instagrammers) {
                    bottomBarPresenter.saveInstagrammerClicked();
                }
            }
        });
        bottomBar.selectTabAtPosition(3, true);
        justInitialized = false;
    }


    @Override
    protected void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showVideos(List<VideoModel> videoModels) {
        adapter.setVideos(videoModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void playVideo(VideoModel videoModel) {
        MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        Uri video = Uri.parse(videoModel.getUrl());
        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(video);
        videoView.start();
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showSavePictureDialog() {

    }

    @Override
    public void showPicture(String pictureUrl) {

    }

    @Override
    public void showSaveInstagrammerDialog() {

    }

    @Override
    public void showSavedInstagrammer() {

    }
}
