package com.losextraditables.bu.videos.view.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losextraditables.bu.R;
import com.losextraditables.bu.videos.view.activity.holder.VideoHolder;
import com.losextraditables.bu.videos.view.activity.model.VideoModel;

import java.util.List;

public class VideoAdapter  extends RecyclerView.Adapter<VideoHolder>{

    private final Context context;
    private List<VideoModel> videos;
    private VideoItemClickListener itemClickListener;

    public VideoAdapter(Context context, VideoItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_video, parent, false);
        return new VideoHolder(v, context, itemClickListener);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        holder.render(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    public void setVideos (List<VideoModel> videos) {
        this.videos = videos;
    }
}
