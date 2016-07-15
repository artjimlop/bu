package com.losextraditables.bu.instagrammers.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.holder.UserViewHolder;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.pictures.view.adapter.OnInstagrammerClickListener;
import java.util.List;

public class InstagrammersAdapter extends RecyclerView.Adapter<UserViewHolder> {

  private final Context context;
  private final OnInstagrammerClickListener onItemLongClickListener;
  private List<InstagrammerModel> instagrammers;
  private InstagrammersListPresenter.ItemClickListener itemClickListener;

  public InstagrammersAdapter(Context context,
      InstagrammersListPresenter.ItemClickListener itemClickListener,
      OnInstagrammerClickListener onItemLongClickListener) {
    this.context = context;
    this.itemClickListener = itemClickListener;
    this.onItemLongClickListener = onItemLongClickListener;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_instagrammer_list, parent, false);
    return new UserViewHolder(v, context, itemClickListener);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, int position) {
    holder.render(instagrammers.get(position), onItemLongClickListener);
  }

  @Override
  public int getItemCount() {
    return instagrammers != null ? instagrammers.size() : 0;
  }

  public void setUsers(List<InstagrammerModel> users) {
    instagrammers = users;
  }
}
