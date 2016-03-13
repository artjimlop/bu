package com.losextraditables.bu.instagrammers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.holder.UserViewHolder;
import com.losextraditables.bu.instagrammers.viewmodel.UserModel;

import java.util.List;

public class InstagrammersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<UserModel> instagrammers;

    public InstagrammersAdapter() {
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instagrammer_list_adapter, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.render(instagrammers.get(position));

    }

    @Override
    public int getItemCount() {
        return instagrammers.size();
    }

    public void setUsers(List<UserModel> users) {
        instagrammers = users;
    }
}
