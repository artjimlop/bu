package com.losextraditables.bu.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.losextraditables.bu.R;
import java.util.List;

public class DialogAdapter extends PagerAdapter {

  private final Context context;
  private List<Integer> images;

  public DialogAdapter(Context context, List<Integer> images) {
    this.context = context;
    this.images = images;
  }

  @Override public int getCount() {
    return images.size();
  }

  public Integer getItemByPosition(Integer position) {
    return images.get(position);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View viewLayout = inflater.inflate(R.layout.item_dialog_pager, container,
        false);

    setupImage(position, viewLayout);

    container.addView(viewLayout);

    return viewLayout;
  }

  private void setupImage(int position, View viewLayout) {
    final ImageView image;
    image = (ImageView) viewLayout.findViewById(R.id.image_dialog);
    image.setBackgroundResource(images.get(position));
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((RelativeLayout) object);
  }
}
