package com.losextraditables.bu.videos.view.activity.holder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.losextraditables.bu.R;
import com.losextraditables.bu.videos.view.activity.adapter.VideoItemClickListener;
import com.losextraditables.bu.videos.view.activity.model.VideoModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final Context context;
    @Bind(R.id.imageview)
    ImageView videoFrame;

    private VideoItemClickListener videoItemClickListener;
    private VideoModel videoModel;

    public VideoHolder(View itemView, Context context, VideoItemClickListener videoItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.videoItemClickListener = videoItemClickListener;
        this.context = context;
    }

    public void render (VideoModel videoModel) {
        this.videoModel = videoModel;
        Picasso.with(context).load(videoModel.getImage()).into(videoFrame);
        videoFrame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        videoItemClickListener.onItemClick(videoModel);
    }
}
