package com.losextraditables.bu.base.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.karumi.rosie.view.RosieAppCompatActivity;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.BaseModule;
import com.losextraditables.bu.utils.SessionManager;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public abstract class BuAppCompatActivity extends RosieAppCompatActivity {

  @Inject SessionManager sessionManager;

  @Override protected abstract int getLayoutId();

  public void showGenericError() {
    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar.make(rootView, getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
  }

  public void showConnectionError() {
    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar.make(rootView, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show();
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new BaseModule(this));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!sessionManager.hasSession()) {
      redirectToLogin();
    }
  }

  protected abstract void redirectToLogin();
}
