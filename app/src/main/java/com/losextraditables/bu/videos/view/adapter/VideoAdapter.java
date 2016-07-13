package com.losextraditables.bu.videos.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.losextraditables.bu.R;
import com.losextraditables.bu.videos.view.holder.VideoViewHolder;
import com.losextraditables.bu.videos.view.model.VideoModel;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

  private final Context context;

  private List<VideoModel> videoModels;

  public VideoAdapter(Context context) {
    this.context = context;
  }

  public void setVideoList(List<VideoModel> videoList) {
    this.videoModels = videoList;
  }

  @Override public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
    return new VideoViewHolder(v, context);
  }

  @Override public void onBindViewHolder(VideoViewHolder holder, int position) {
    holder.render(videoModels.get(position));
  }

  @Override public int getItemCount() {
    return videoModels != null ? videoModels.size() : 0;
  }
}
