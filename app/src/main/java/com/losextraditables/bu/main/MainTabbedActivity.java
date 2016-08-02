package com.losextraditables.bu.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.instagrammers.view.activity.InstagrammersFragment;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.losextraditables.bu.videos.view.activity.VideoFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import java.util.Collections;
import java.util.List;

public class MainTabbedActivity extends BuAppCompatActivity {

  private Fragment currentFragment;

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
    setupBottomBar(savedInstanceState, this);
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setMaxFixedTabs(2);
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
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
        ((BaseFragment)currentFragment).scrollListToTop();
      }
    });
  }

  protected void switchTab(Fragment fragment) {
    getFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getName())
        .commit();
  }

  @Override protected void onPause() {
    super.onPause();
    JCVideoPlayer.releaseAllVideos();
  }
}
