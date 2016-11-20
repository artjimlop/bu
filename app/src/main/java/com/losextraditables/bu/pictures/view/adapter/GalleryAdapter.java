package com.losextraditables.bu.pictures.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.losextraditables.bu.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GalleryAdapter extends PagerAdapter {

  private final Context context;
  private List<String> images;
  private LayoutInflater inflater;

  public GalleryAdapter(Context context, List<String> images) {
    this.context = context;
    this.images = images;
  }

  @Override public int getCount() {
    if (images != null) {
      return images.size();
    } else {
      return 0;
    }
  }

  public String getItemByPosition(Integer position) {
    return images.get(position);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View viewLayout = inflater.inflate(R.layout.page_fullscreen_image, container,
        false);

    setupImage(position, viewLayout);

    container.addView(viewLayout);

    return viewLayout;
  }

  private void setupImage(int position, View viewLayout) {
    final ImageView image;
    image = (ImageView) viewLayout.findViewById(R.id.picture);
    Picasso.with(context).load(images.get(position)).into(image, new Callback() {
      @Override public void onSuccess() {
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        PhotoViewAttacher attacher = new PhotoViewAttacher(image, true);
        attacher.update();
      }

      @Override public void onError() {
        /* no-op */
      }
    });
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((RelativeLayout) object);
  }
}
