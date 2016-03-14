package com.losextraditables.bu.instagrammers.view.holder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.model.InstagrammerModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    @Bind(R.id.instagrammer_picture)
    ImageView picture;
    @Bind(R.id.instagrammer_name)
    TextView name;
    @Bind(R.id.instagrammer_username)
    TextView username;

    public UserViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
    }

    public void render(final InstagrammerModel instagrammerModel) {
        name.setText(instagrammerModel.getFullName());
        username.setText(instagrammerModel.getUserName());
        Picasso.with(context).load(instagrammerModel.getProfilePicture()).into(picture);
    }
}
