package com.losextraditables.bu.videos.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.bottombar.view.BottomBarPresenter;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.VideosModule;
import com.losextraditables.bu.videos.view.adapter.VideoAdapter;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.presenter.VideoListPresenter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class VideoActivity extends BuAppCompatActivity
    implements BottomBarPresenter.View, VideoListPresenter.View {

  @Bind(R.id.videos) RecyclerView videoRecycler;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject @Presenter VideoListPresenter presenter;
  @Inject @Presenter BottomBarPresenter bottomBarPresenter;
  @Inject SessionManager session;

  private VideoAdapter adapter;
  private LinearLayoutManager linearLayoutManager;
  private BottomBar bottomBar;
  private boolean justInitialized = true;

  @Override protected int getLayoutId() {
    return R.layout.activity_video;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Collections.singletonList((Object) new VideosModule());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    setupAdapter();
    presenter.showVideos(session.getUid());
    setupBottomBar(savedInstanceState, this);
  }

  @Override protected void onPreparePresenter() {
    super.onPreparePresenter();
    presenter.initialize();
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_videos) {
          if (!justInitialized) {
            bottomBarPresenter.showVideosClicked();
          }
        } else if (menuItemId == R.id.bottom_save_instagrammers) {
          bottomBarPresenter.saveInstagrammerClicked();
        } else if (menuItemId == R.id.bottom_pictures) {
          if (!justInitialized) {
            startActivity(new Intent(context, PicturesActivity.class));
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            finish();
          }
        } else if (menuItemId == R.id.bottom_instagrammers) {
          startActivity(new Intent(context, InstagrammersListActivity.class));
          overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
          finish();
        }
      }

      @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
        } else if (menuItemId == R.id.bottom_save_instagrammers) {
          bottomBarPresenter.saveInstagrammerClicked();
        }
      }
    });
    bottomBar.selectTabAtPosition(1, true);
    justInitialized = false;
  }

  private void setupAdapter() {
    adapter = new VideoAdapter(this);
    videoRecycler.setAdapter(adapter);
    linearLayoutManager = new LinearLayoutManager(this);
    videoRecycler.setLayoutManager(linearLayoutManager);
  }

  @Override protected void redirectToLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override public void showVideos() {
    /* no-op */
  }

  @Override public void showPicture(String pictureUrl) {
    startActivity(PictureActivity.getIntentForActivity(this, pictureUrl));
  }

  @Override public void showSaveInstagrammerDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert instagrammers's url here").setTitle("Save");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    final Context context = this;
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        bottomBarPresenter.saveUser(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
  }

  @Override public void showSavedInstagrammer() {
    /* no-op */
  }

  @Override public void showVideos(List<VideoModel> videoModels) {
    adapter.setVideoList(videoModels);
    adapter.notifyDataSetChanged();
  }

  @Override public void showAddVideoDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert video's url here").setTitle("Save video");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    final Context context = this;
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        presenter.saveVideo(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
  }

  @Override public void refresh() {
    presenter.showVideos(session.getUid());
  }

  @Override public void hideLoading() {
    /*no-op*/
  }

  @Override public void showLoading() {
    /*no-op*/
  }

  @Override protected void onPause() {
    super.onPause();
    JCVideoPlayer.releaseAllVideos();
  }

  @OnClick(R.id.fab) void onFabClick() {
    presenter.onAddVideoClick();
  }
}
