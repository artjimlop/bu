package com.losextraditables.bu.pictures.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.transition.Explode;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.PicturesModule;
import com.losextraditables.bu.pictures.view.presenter.PicturesPresenter;
import com.losextraditables.bu.utils.InstagramSession;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class PicturesActivity extends BuAppCompatActivity
    implements PicturesPresenter.View {

  @Bind(R.id.pictures_list)
  RecyclerView picturesList;

  @Inject
  @Presenter
  PicturesPresenter presenter;

  @Inject InstagramSession session;

  private BottomBar bottomBar;

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
    setupWindowAnimations();

    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          presenter.saveInstagrammerClicked();
        }

      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          presenter.saveInstagrammerClicked();
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
