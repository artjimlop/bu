package com.losextraditables.bu.pictures.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.losextraditables.bu.R;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SavedPicturesAdapter extends BaseAdapter {
  private final OnItemLongClickListener onItemLongClickListener;
  private Context context;
  private List<PictureModel> pictureModels;
  private ItemClickListener itemClickListener;

  public SavedPicturesAdapter(Context context, List<PictureModel> pictureModels,
      ItemClickListener itemClickListener,
      OnItemLongClickListener onItemLongClickListener) {
    this.context = context;
    this.pictureModels = pictureModels;
    this.itemClickListener = itemClickListener;
    this.onItemLongClickListener = onItemLongClickListener;
  }

  @Override public int getCount() {
    return pictureModels.size();
  }

  @Override public PictureModel getItem(int position) {
    return pictureModels.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(final int position, View view, ViewGroup viewGroup) {
    if (view == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.item_saved_picture, viewGroup, false);
    }

    ImageView picture = (ImageView) view.findViewById(R.id.picture);
    picture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        itemClickListener.onItemClick(view, position);
      }
    });
    picture.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        onItemLongClickListener.onItemLongClick(v, pictureModels.get(position).getUrl());
        return false;
      }
    });

    final PictureModel item = getItem(position);
    Picasso.with(context)
        .load(item.getUrl())
        .noFade()
        .placeholder(R.drawable.no_image_placeholder)
        .into(picture);
    return view;
  }

  public ArrayList<String> getImagesUrls() {
    ArrayList<String> urls = new ArrayList<>();
    for (PictureModel pictureModel : pictureModels) {
      urls.add(pictureModel.getUrl());
    }
    return urls;
  }
}

