package com.losextraditables.bu.videos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.losextraditables.bu.R;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClick;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClickListener;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {

  private final Context context;
  private final OnVideoClickListener onVideoClickListener;
  private final OnVideoClick onVideoClick;

  private List<VideoModel> videoModels;

  public VideoAdapter(Context context, OnVideoClickListener onVideoClickListener,
      OnVideoClick onVideoClick) {
    this.context = context;
    this.onVideoClickListener = onVideoClickListener;
    this.onVideoClick = onVideoClick;
  }

  public void setVideoList(List<VideoModel> videoList) {
    this.videoModels = videoList;
  }

  //@Override public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  //  View v =
  //      LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
  //  return new VideoViewHolder(v, context);
  //}
  //
  //@Override public void onBindViewHolder(VideoViewHolder holder, int position) {
  //  holder.render(videoModels.get(position), onVideoClickListener, onVideoClick);
  //}
  //
  //@Override public int getItemCount() {
  //  return videoModels != null ? videoModels.size() : 0;
  //}

  @Override public int getCount() {
    if (videoModels != null) {
      return videoModels.size();
    } else {
      return 0;
    }
  }

  @Override public VideoModel getItem(int position) {
    return videoModels.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(final int position, View view, ViewGroup viewGroup) {
    if (view == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.item_saved_video, viewGroup, false);
    }

    ImageView picture = (ImageView) view.findViewById(R.id.picture);
    picture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onVideoClick.onItemClick(view, videoModels.get(position));
      }
    });
    picture.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        onVideoClickListener.onItemLongClick(v, videoModels.get(position).getUrl());
        return false;
      }
    });

    final VideoModel item = getItem(position);
    Picasso.with(context)
        .load(item.getImage())
        .noFade()
        .placeholder(R.drawable.no_resource_placeholder)
        .into(picture);
    return view;
  }

  public ArrayList<String> getImagesUrls() {
    ArrayList<String> urls = new ArrayList<>();
    for (VideoModel videoModel : videoModels) {
      urls.add(videoModel.getUrl());
    }
    return urls;
  }
}
