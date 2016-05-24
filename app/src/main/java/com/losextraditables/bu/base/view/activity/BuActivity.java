package com.losextraditables.bu.base.view.activity;

import android.os.Bundle;
import com.karumi.rosie.view.RosieActivity;
import com.losextraditables.bu.base.view.BaseModule;
import com.losextraditables.bu.utils.SessionManager;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public abstract class BuActivity extends RosieActivity {

  @Inject SessionManager instagramSession;

  @Override protected abstract int getLayoutId();

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new BaseModule(this));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!instagramSession.hasSession()) {
      redirectToLogin();
    }
  }

  protected abstract void redirectToLogin();
}


