package com.losextraditables.bu.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.login.view.activity.LoginActivity;
import com.losextraditables.bu.pictures.view.activity.PicturesFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.Collections;
import java.util.List;

public class MainTabbedActivity extends BuAppCompatActivity {

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
    setupBottomBar(savedInstanceState, this);
  }

  private void setupBottomBar(Bundle savedInstanceState, final Context context) {
    bottomBar = BottomBar.attach(this, savedInstanceState);
    bottomBar.noTopOffset();
    bottomBar.noNavBarGoodness();
    bottomBar.setMaxFixedTabs(2);
    bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        /*if (menuItemId == R.id.bottom_videos) {
          bottomBarPresenter.showVideosClicked();
        } else if (menuItemId == R.id.bottom_instagrammers) {
          startActivity(new Intent(context, InstagrammersListActivity.class));
          overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
          finish();
        }*/

        switch (menuItemId) {
          case R.id.bottom_pictures:
            PicturesFragment picturesFragment = PicturesFragment.newInstance();
            //currentFragment = picturesFragment;
            switchTab(picturesFragment);
            break;
          case R.id.bottom_videos:
            /*Fragment favoritesFragment = FavoritesFragment.newInstance();
            currentFragment = favoritesFragment;
            switchTab(favoritesFragment);*/
            break;
          case R.id.bottom_instagrammers:
            /*Fragment discoverFragment = DiscoverFragment.newInstance();
            currentFragment = discoverFragment;
            switchTab(discoverFragment);*/
            break;
          default:
            break;
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        //TODO
      }
    });
  }

  protected void switchTab(Fragment fragment) {
    getFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getName())
        .commit();
  }
}
