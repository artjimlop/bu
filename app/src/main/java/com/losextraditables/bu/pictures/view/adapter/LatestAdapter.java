package com.losextraditables.bu.pictures.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.losextraditables.bu.R;
import com.losextraditables.bu.pictures.model.LatestItemModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class LatestAdapter extends BaseAdapter {
  private final OnItemLongClickListener onItemLongClickListener;
  private Context context;
  private List<LatestItemModel> latestItemModels;
  private ItemClickListener itemClickListener;

  public LatestAdapter(Context context, List<LatestItemModel> latestItemModels,
      ItemClickListener itemClickListener,
      OnItemLongClickListener onItemLongClickListener) {
    this.context = context;
    this.latestItemModels = latestItemModels;
    this.itemClickListener = itemClickListener;
    this.onItemLongClickListener = onItemLongClickListener;
  }

  @Override public int getCount() {
    return latestItemModels.size();
  }

  @Override public LatestItemModel getItem(int position) {
    return latestItemModels.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(final int position, View view, ViewGroup viewGroup) {
    final LatestItemModel item = getItem(position);
    if (view == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      if (item.getHasPicture()) {
        view = inflater.inflate(R.layout.item_saved_picture, viewGroup, false);
      } else {
        view = inflater.inflate(R.layout.item_latest_video, viewGroup, false);
      }
    }

    ImageView picture = (ImageView) view.findViewById(R.id.picture);
    picture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        itemClickListener.onItemClick(view, position);
      }
    });
    picture.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        LatestItemModel latestItemModel = latestItemModels.get(position);
        if (latestItemModel.getHasPicture()) {
          onItemLongClickListener.onItemLongClick(v, latestItemModel.getPicture().getUrl());
        } else {
          onItemLongClickListener.onItemLongClick(v, latestItemModel.getVideo().getUrl());
        }
        return false;
      }
    });

    if (item.getHasPicture()) {
      Picasso.with(context)
          .load(item.getPicture().getUrl())
          .noFade()
          .placeholder(R.drawable.no_resource_placeholder)
          .into(picture);
    } else {
      Picasso.with(context)
          .load(item.getVideo().getImage())
          .noFade()
          .placeholder(R.drawable.no_resource_placeholder)
          .into(picture);
    }
    return view;
  }

  public ArrayList<String> getImagesUrls() {
    ArrayList<String> urls = new ArrayList<>();
    for (LatestItemModel pictureModel : latestItemModels) {
      urls.add(pictureModel.getPicture().getUrl());
    }
    return urls;
  }
}

