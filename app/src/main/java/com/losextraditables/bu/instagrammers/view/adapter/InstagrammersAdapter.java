package com.losextraditables.bu.instagrammers.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.holder.UserViewHolder;

import java.util.List;

public class InstagrammersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final Context context;
    private List<InstagrammerModel> instagrammers;

    public InstagrammersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instagrammer_list_adapter, parent, false);
        return new UserViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.render(instagrammers.get(position));

    }

    @Override
    public int getItemCount() {
        return instagrammers.size();
    }

    public void setUsers(List<InstagrammerModel> users) {
        instagrammers = users;
    }
}
