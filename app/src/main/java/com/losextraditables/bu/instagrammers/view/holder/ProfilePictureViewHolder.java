package com.losextraditables.bu.instagrammers.view.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.adapter.OnProfilePictureClickListener;
import com.squareup.picasso.Picasso;

public class ProfilePictureViewHolder extends RecyclerView.ViewHolder {

  private final Context context;
  private final OnProfilePictureClickListener onProfilePictureClickListener;
  @Bind(R.id.profile_picture) ImageView picture;

  public ProfilePictureViewHolder(View itemView, Context context,
      OnProfilePictureClickListener onProfilePictureClickListener) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.context = context;
    this.onProfilePictureClickListener = onProfilePictureClickListener;
  }

  public void render(final String pictureUrl) {
    Picasso.with(context).load(pictureUrl).into(picture);
    picture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onProfilePictureClickListener.onItemClick(pictureUrl);
      }
    });
  }

  public ImageView getImage(){
    return picture;
  }
}