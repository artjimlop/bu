package com.losextraditables.bu.instagrammers.view.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.model.UserModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.instagrammer_picture)
    View picture;
    @Bind(R.id.instagrammer_name)
    TextView name;
    @Bind(R.id.instagrammer_username)
    TextView username;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void render(final UserModel userModel) {
        //TODO use glide for rendering picture
        name.setText(userModel.getFullName());
        username.setText(userModel.getUserName());
    }
}
