package com.losextraditables.bu.instagrammers.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstagrammerDetailActivity extends AppCompatActivity {


    public static final String USERNAME = "username";
    public static final String PHOTO = "photo";
    @Bind(R.id.user_avatar)
    ImageView userPhoto;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private InstagrammerModel instagrammerModel;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void init(Activity activity, View sharedView, InstagrammerModel instagrammerModel) {
        Intent intent = new Intent(activity, InstagrammerDetailActivity.class);
        intent.putExtra(USERNAME, instagrammerModel.getUserName());
        intent.putExtra(PHOTO, instagrammerModel.getProfilePicture());

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, sharedView.getTransitionName());
        activity.startActivity(intent, activityOptions.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagrammer_detail);
        ButterKnife.bind(this);
        setupToolbar();
        setupViews();
    }

    private void setupToolbar(){
        String username = getIntent().getStringExtra(USERNAME);
        String photo = getIntent().getStringExtra(PHOTO);

        toolbar.setTitle(username);
        setSupportActionBar(toolbar);

        Picasso.with(this).load(photo).into(userPhoto);
        userPhoto.setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.DARKEN);
    }

    private void setupViews(){
        Window window = getWindow();
        Transition transition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            transition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.detail_enter_transition);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setEnterTransition(transition);
        }
    }

}
