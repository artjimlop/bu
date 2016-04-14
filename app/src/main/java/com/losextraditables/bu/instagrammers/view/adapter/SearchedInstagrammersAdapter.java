package com.losextraditables.bu.instagrammers.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.holder.SearchViewHolder;
import com.losextraditables.bu.instagrammers.view.model.SearchedInstagrammerModel;
import java.util.List;

public class SearchedInstagrammersAdapter extends RecyclerView.Adapter<SearchViewHolder> {

  private final Context context;
  private List<SearchedInstagrammerModel> instagrammers;

  public SearchedInstagrammersAdapter(Context context) {
    this.context = context;
  }

  @Override
  public SearchViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_instagrammer_list_adapter, parent, false);
    return new SearchViewHolder(v, context);
  }

  @Override
  public void onBindViewHolder(SearchViewHolder holder, int position) {
    holder.render(instagrammers.get(position));
  }

  @Override
  public int getItemCount() {
    return instagrammers != null ? instagrammers.size() : 0;
  }

  public void setUsers(List<SearchedInstagrammerModel> models) {
    instagrammers = models;
  }
}
