package com.losextraditables.bu.base.view.activity;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.karumi.rosie.view.RosieActivity;
import com.losextraditables.bu.R;

public abstract class BuActivity extends RosieActivity {

    @Override protected abstract int getLayoutId();

    public void showGenericError() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
    }

    public void showConnectionError() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show();
    }

}


