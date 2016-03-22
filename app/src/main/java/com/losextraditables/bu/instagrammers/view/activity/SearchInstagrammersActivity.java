package com.losextraditables.bu.instagrammers.view.activity;

import android.content.Intent;

import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuActivity;
import com.losextraditables.bu.login.activity.LoginActivity;

public class SearchInstagrammersActivity extends BuActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_instagrammers;
    }

    @Override
    protected void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
