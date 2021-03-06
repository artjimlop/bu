package com.losextraditables.bu.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersFragment;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.LatestFragment;
import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.losextraditables.bu.utils.Intents;
import com.losextraditables.bu.videos.view.activity.VideoFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import java.util.Collections;
import java.util.List;

public class MainTabbedActivity extends BuAppCompatActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  private Fragment currentFragment;
  private BottomBar bottomBar;

  public static Intent getIntentForActivity(Context context) {
    return new Intent(context, MainTabbedActivity.class);
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Collections.singletonList((Object) new MainModule());
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_main_tabbed;
  }

  @Override protected void redirectToLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tabbed);
    ButterKnife.bind(this);
    setupBottomBar(savedInstanceState, this);
    setupRateAppDialog();
  }

  public void setUpToolbar(boolean showUpButton, String title) {
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(showUpButton);
        toolbar.setTitle(title);
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.textColorPrimary));
      }
    }
  }

  private void setupRateAppDialog() {
    AppRate.with(this)
            .setInstallDays(1)
            .setShowLaterButton(true)
            .setOnClickButtonListener(new OnClickButtonListener() {
              @Override
              public void onClickButton(int which) {
                Log.d(MainTabbedActivity.class.getName(), Integer.toString(which));
              }
            })
            .monitor();

    AppRate.showRateDialogIfMeetsConditions(this);
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setMaxFixedTabs(3);
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
          case R.id.bottom_latest:
            Fragment latestFragment = LatestFragment.newInstance();
            currentFragment = latestFragment;
            switchTab(latestFragment);
            break;
          case R.id.bottom_pictures:
            Fragment picturesFragment = PicturesFragment.newInstance();
            currentFragment = picturesFragment;
            switchTab(picturesFragment);
            break;
          case R.id.bottom_videos:
            Fragment videoFragment = VideoFragment.newInstance();
            currentFragment = videoFragment;
            switchTab(videoFragment);
            break;
          case R.id.bottom_instagrammers:
            Fragment instagrammerFragment = InstagrammersFragment.newInstance();
            currentFragment = instagrammerFragment;
            switchTab(instagrammerFragment);
            break;
          default:
            break;
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        ((BaseFragment) currentFragment).scrollListToTop();
      }
    });
    bottomBar.mapColorForTab(0, "#6F88A2");
    bottomBar.mapColorForTab(1, "#6F88A2");
    bottomBar.mapColorForTab(2, "#6F88A2");
    bottomBar.mapColorForTab(3, "#6F88A2");
  }

  protected void switchTab(Fragment fragment) {
    getFragmentManager().beginTransaction()
        .replace(R.id.container, fragment, fragment.getClass().getName())
        .commit();
  }

  @Override protected void onPause() {
    super.onPause();
    JCVideoPlayer.releaseAllVideos();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_share, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      return true;
    } else if (item.getItemId() == R.id.menu_share) {
      shareApp();
    }
    return super.onOptionsItemSelected(item);
  }

  private void shareApp() {
    String subject = getString(R.string.share_app_subject);
    String message = getString(R.string.share_app_message);
    String url = getString(R.string.share_app_website);

    String sharedText = message + " " + url;

    Intent chooserIntent = ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setSubject(subject)
        .setText(sharedText)
        .setChooserTitle(R.string.share_app_chooser_title)
        .createChooserIntent();
    Intents.maybeStartActivity(this, chooserIntent);
  }
}
