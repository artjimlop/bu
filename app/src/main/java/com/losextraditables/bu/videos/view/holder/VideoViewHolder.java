package com.losextraditables.bu.videos.view.holder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClick;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClickListener;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.squareup.picasso.Picasso;

public class VideoViewHolder extends RecyclerView.ViewHolder {

  private static final String EMPTY_STRING = "";
  @Bind(R.id.video_title) TextView title;
  @Bind(R.id.container) CardView container;
  @Bind(R.id.video) ImageView videoPlayer;

  private final Context context;

  public VideoViewHolder(View itemView, Context context) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.context = context;
  }

  public void render(final VideoModel videoModel, final OnVideoClickListener onVideoClickListener,
      final OnVideoClick onVideoClick) {
    title.setText(videoModel.getTitle());
    if (videoModel.getImage() != null) {
      Picasso.with(context).load(videoModel.getImage()).into(videoPlayer);
    }
    videoPlayer.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onVideoClick.onItemClick(v, videoModel);
      }
    });
    container.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        onVideoClickListener.onItemLongClick(v, videoModel.getUrl());
        return false;
      }
    });
  }
}
