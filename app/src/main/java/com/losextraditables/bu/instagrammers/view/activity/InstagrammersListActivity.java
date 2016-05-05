package com.losextraditables.bu.instagrammers.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.transition.Explode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.instagrammers.view.adapter.InstagrammersAdapter;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PictureActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesActivity;
import com.losextraditables.bu.utils.InstagramSession;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class InstagrammersListActivity extends BuAppCompatActivity
    implements InstagrammersListPresenter.View {

  @Bind(R.id.instagrammers_list)
  RecyclerView instagrammersList;

  @Inject
  @Presenter
  InstagrammersListPresenter presenter;

  @Inject InstagramSession session;

  private InstagrammersAdapter adapter;
  private LinearLayoutManager linearLayoutManager;
  private View sharedImage;
  private BottomBar bottomBar;

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
    presenter.showInstagrammers(session.getUid(this));

    final Context context = this;

    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_pictures) {
          startActivity(new Intent(context, PicturesActivity.class));
          finish();
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottom_save_picture) {
          presenter.savePictureClicked();
        } else if (menuItemId == R.id.bottom_pictures) {
          startActivity(new Intent(context, PicturesActivity.class));
          finish();
        }
      }
    });
    bottomBar.selectTabAtPosition(3, true);
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

  @Override
  public void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels) {
    adapter.setUsers(instagrammerModels);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
    InstagrammerDetailActivity.init(this, sharedImage, instagrammerModel);
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

  @OnClick(R.id.fab) public void onFabClick() {
    startActivity(new Intent(this, SearchInstagrammersActivity.class));
  }
}
