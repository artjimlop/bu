package com.losextraditables.bu.pictures.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Explode;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.bottombar.view.BottomBarPresenter;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.losextraditables.bu.pictures.view.adapter.ItemClickListener;
import com.losextraditables.bu.pictures.view.adapter.SavedPicturesAdapter;
import com.losextraditables.bu.pictures.view.presenter.PicturesPresenter;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.view.activity.VideoActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class PicturesActivity extends BuAppCompatActivity
    implements BottomBarPresenter.View, PicturesPresenter.View {

  @Bind(R.id.pictures_list) GridView picturesList;
  @Bind(R.id.saved_pictures_progress) ProgressBar progressBar;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject
  @Presenter
  PicturesPresenter picturesPresenter;

  @Inject
  @Presenter
  BottomBarPresenter bottomBarPresenter;

  @Inject SessionManager session;

  private BottomBar bottomBar;
  private SavedPicturesAdapter adapter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_pictures;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new PicturesModule());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pictures);
    ButterKnife.bind(this);
    setupWindowAnimations();
    setupToolbar();
    final Context context = this;
    setupBottomBar(savedInstanceState, context);

    picturesPresenter.loadSavedPictures(session.getUid());
  }

  private void setupToolbar() {
    toolbar.setTitle(this.getResources().getString(R.string.pictures_activity));
    setSupportActionBar(toolbar);
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setMaxFixedTabs(2);
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          startActivity(new Intent(context, InstagrammersListActivity.class));
          overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
          finish();
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          startActivity(new Intent(context, InstagrammersListActivity.class));
          finish();
        }
      }
    });
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    bottomBar.onSaveInstanceState(outState);
  }

  @Override
  protected void redirectToLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override protected void onPreparePresenter() {
    super.onPreparePresenter();
    picturesPresenter.initialize();
    bottomBarPresenter.initialize();
  }

  @Override public void showVideos() {
    startActivity(new Intent(this, VideoActivity.class));
    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    finish();
  }

  @Override public void showPicture(String pictureUrl) {
    startActivity(PictureActivity.getIntentForActivity(this, pictureUrl));
  }

  @Override public void showSaveInstagrammerDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert instagrammers's url here")
        .setTitle("Save");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    final Context context = this;
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        bottomBarPresenter.saveUser(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
  }

  @Override public void showSavedInstagrammer() {
  }

  @Override public void showSavedPictures(List<PictureModel> pictures) {
    adapter = new SavedPicturesAdapter(this, pictures, new ItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        goToSavedPictureActivity(view, position);
      }
    });
    picturesList.setAdapter(adapter);
  }

  @Override public void showSavePictureDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert picture's url here")
        .setTitle("Save picture");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        bottomBarPresenter.savePicture(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
  }

  private void goToSavedPictureActivity(View view, int position) {
    PictureActivity.init(this, view, adapter.getItem(position).getUrl());
  }

  @Override
  public void hideLoading() {
    picturesList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showLoading() {
    picturesList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void setupWindowAnimations() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setReenterTransition(new Explode());
      getWindow().setExitTransition(new Explode().setDuration(500));
    }
  }

  @Override protected void onResume() {
    super.onResume();
    picturesPresenter.loadSavedPictures(session.getUid());
  }

  @OnClick(R.id.fab) void onFabClick() {
    picturesPresenter.onAddPictureClick();
  }
}
