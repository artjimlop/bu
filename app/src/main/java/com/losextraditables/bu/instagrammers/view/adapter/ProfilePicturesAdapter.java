package com.losextraditables.bu.instagrammers.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.holder.ProfilePictureViewHolder;
import java.util.ArrayList;
import java.util.List;

public class ProfilePicturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<String> items;
  private final OnProfilePictureClickListener onProfilePictureClickListener;
  private final Context context;

  public ProfilePicturesAdapter(Context context, OnProfilePictureClickListener onProfilePictureClickListener) {
    this.onProfilePictureClickListener = onProfilePictureClickListener;
    this.context = context;
    items = new ArrayList<>();
  }

  public void setItems(List<String> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_picture, parent, false);
    return new ProfilePictureViewHolder(view, context, onProfilePictureClickListener);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((ProfilePictureViewHolder) holder).render(items.get(position));
  }

  @Override public int getItemCount() {
    return items.size();
  }
}
