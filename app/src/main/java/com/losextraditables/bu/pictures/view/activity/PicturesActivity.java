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
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersListActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.pictures.view.adapter.ItemClickListener;
import com.losextraditables.bu.pictures.view.adapter.SavedPicturesAdapter;
import com.losextraditables.bu.pictures.view.presenter.PicturesPresenter;
import com.losextraditables.bu.utils.InstagramSession;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class PicturesActivity extends BuAppCompatActivity
    implements PicturesPresenter.View {

  @Bind(R.id.pictures_list) GridView picturesList;
  @Bind(R.id.saved_pictures_progress) ProgressBar progressBar;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject
  @Presenter
  PicturesPresenter presenter;

  @Inject InstagramSession session;

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
    setSupportActionBar(toolbar);
    final Context context = this;
    setupBottomBar(savedInstanceState, context);

    presenter.loadSavedPictures();
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_save_instagrammers) {
          presenter.saveInstagrammerClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          startActivity(new Intent(context, InstagrammersListActivity.class));
          finish();
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_save_instagrammers) {
          presenter.saveInstagrammerClicked();
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
    presenter.initialize();
  }

  @Override public void showSavePictureDialog() {
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
        presenter.savePicture(input.getText().toString(), session.getUid(context));
      }
    });

    builder.create().show();
  }

  @Override public void showPicture(String pictureUrl) {
    Toast.makeText(this, pictureUrl, Toast.LENGTH_LONG).show();
    startActivity(PictureActivity.getIntentForActivity(this, pictureUrl));
  }

  @Override public void showSaveInstagrammerDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Insert user's url here")
        .setTitle("Save user");

    final EditText input = new EditText(this);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    final Context context = this;
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        presenter.saveUser(input.getText().toString(), session.getUid(context));
      }
    });

    builder.create().show();
  }

  @Override public void showSavedInstagrammer() {
    Toast.makeText(this, "User saved", Toast.LENGTH_LONG).show();
  }

  @Override public void showSavedPictures(ArrayList<String> urls) {
    adapter = new SavedPicturesAdapter(this, urls, new ItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        goToSavedPictureActivity(view, position);
      }
    });
    picturesList.setAdapter(adapter);
  }

  private void goToSavedPictureActivity(View view, int position) {
    SavedPictureActivity.init(this, view, adapter.getItem(position));
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
    getWindow().setReenterTransition(new Explode());
    getWindow().setExitTransition(new Explode().setDuration(500));
  }

}
