package com.losextraditables.bu.instagrammers.view.holder;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.losextraditables.bu.R;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    @Bind(R.id.user_blur_image)
    ImageView picture;
    @Bind(R.id.blur_layout)
    BlurLayout blurLayout;

    View hover;
    TextView username;
    ImageView avatar;

    public UserViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        buildHoverView();
        picture.setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.SRC_ATOP);
    }

    private void buildHoverView() {
        hover = LayoutInflater.from(context).inflate(R.layout.instagrammer_hover_item_list, null);
        username = (TextView) hover.findViewById(R.id.user_name);
        avatar =(CircleImageView) hover.findViewById(R.id.user_avatar);
        blurLayout.setHoverView(hover);
        blurLayout.addChildAppearAnimator(hover, R.id.user_name, Techniques.FadeInUp);
        blurLayout.addChildDisappearAnimator(hover, R.id.user_name, Techniques.FadeOutDown);
        blurLayout.addChildAppearAnimator(hover, R.id.user_avatar, Techniques.DropOut, 1200);
        blurLayout.addChildDisappearAnimator(hover, R.id.user_avatar, Techniques.FadeOutUp);
        blurLayout.setBlurDuration(1000);
    }

    public void render(final InstagrammerModel instagrammerModel) {
        username.setText(instagrammerModel.getFullName());
        username.setText(instagrammerModel.getUserName());
        Picasso.with(context).load(instagrammerModel.getProfilePicture()).into(picture);
        Picasso.with(context).load(instagrammerModel.getProfilePicture()).into(avatar);
    }
}
