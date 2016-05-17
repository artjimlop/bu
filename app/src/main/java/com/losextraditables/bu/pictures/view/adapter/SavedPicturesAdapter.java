package com.losextraditables.bu.pictures.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.losextraditables.bu.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SavedPicturesAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> urls;
    private ItemClickListener itemClickListener;

    public SavedPicturesAdapter(Context context, ArrayList<String> urls, ItemClickListener itemClickListener) {
        this.context = context;
        this.urls = urls;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_saved_picture, viewGroup, false);
        }

        ImageView picture = (ImageView) view.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

        final String item = getItem(position);
        Picasso.with(context).load(item).into(picture);
        return view;
    }
}

