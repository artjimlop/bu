package com.losextraditables.bu.instagrammers.view.activity;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.bottombar.view.BottomBarPresenter;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.instagrammers.view.adapter.InstagrammersAdapter;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.SessionManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class InstagrammersListActivity extends BuAppCompatActivity
    implements BottomBarPresenter.View, InstagrammersListPresenter.View {

  private static final int ANIMATION_DURATION = 500;
  @Bind(R.id.instagrammers_list)
  RecyclerView instagrammersList;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Bind(R.id.instagrammers_progress) ProgressBar progressBar;

  @Inject
  @Presenter
  InstagrammersListPresenter instagrammersListPresenter;

  @Inject
  @Presenter
  BottomBarPresenter bottomBarPresenter;

  @Inject SessionManager session;

  private InstagrammersAdapter adapter;
  private LinearLayoutManager linearLayoutManager;
  private View sharedImage;
  private BottomBar bottomBar;

  private boolean justInitialized = true;

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
    setContentView(R.layout.activity_instagrammers);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    adapter = new InstagrammersAdapter(this, new InstagrammersListPresenter.ItemClickListener() {
      @Override
      public void onItemClick(View view, InstagrammerModel instagrammerModel) {
        sharedImage = view.findViewById(R.id.instagrammer_avatar);
        instagrammersListPresenter.goToInstagrammerDetail(instagrammerModel);
      }
    });
    instagrammersList.setAdapter(adapter);
    linearLayoutManager = new LinearLayoutManager(this);
    instagrammersList.setLayoutManager(linearLayoutManager);
    instagrammersListPresenter.showInstagrammers(session.getUid());

    setupBottomBar(savedInstanceState, this);
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
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
        if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
        } else if (menuItemId == R.id.bottom_save_instagrammers) {
          bottomBarPresenter.saveInstagrammerClicked();
        }
      }
    });
    bottomBar.selectTabAtPosition(3, true);
    justInitialized = false;
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
    instagrammersListPresenter.initialize();
  }

  @Override
  public void showInstagrammers(List<InstagrammerModel> instagrammerModels) {
    adapter.setUsers(instagrammerModels);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
    InstagrammerDetailActivity.init(this, sharedImage, instagrammerModel);
  }

  @Override public void showVideos() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert picture's url here")
        .setTitle("Save picture");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    final Context context = this;
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        bottomBarPresenter.savePicture(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
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

  @Override
  public void hideLoading() {
    instagrammersList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showLoading() {
    instagrammersList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

}
