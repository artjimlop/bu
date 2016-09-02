package com.losextraditables.bu.instagrammers.view.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.pictures.view.adapter.OnInstagrammerClickListener;
import com.losextraditables.bu.utils.BlurTransform;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private final Context context;
  @Bind(R.id.instagrammer_blur_image)
  ImageView picture;
  @Bind(R.id.instagrammer_avatar) CircleImageView avatar;
  @Bind(R.id.instagrammer_name) TextView username;
  @Bind(R.id.instagrammer_container) FrameLayout container;

  private InstagrammersListPresenter.ItemClickListener onClickListener;
  private InstagrammerModel instagrammerModel;

  public UserViewHolder(View itemView, Context context,
      InstagrammersListPresenter.ItemClickListener onClickListener) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.context = context;
    this.onClickListener = onClickListener;
  }

  public void render(final InstagrammerModel instagrammerModel,
      final OnInstagrammerClickListener onItemLongClickListener) {
    this.instagrammerModel = instagrammerModel;
    container.setOnClickListener(this);
    container.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        onItemLongClickListener.onItemLongClick(v, instagrammerModel.getUserName());
        return false;
      }
    });
    username.setText(instagrammerModel.getFullName());
    username.setText(instagrammerModel.getUserName());
    Picasso.with(context).load(instagrammerModel.getProfilePicture()).
        transform(new BlurTransform(context)).into(picture);
    Picasso.with(context).load(instagrammerModel.getProfilePicture()).into(avatar);
  }

  @Override
  public void onClick(View view) {
    onClickListener.onItemClick(view, instagrammerModel);
  }
}
