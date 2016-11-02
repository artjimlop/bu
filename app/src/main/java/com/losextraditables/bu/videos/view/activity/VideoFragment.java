package com.losextraditables.bu.videos.view.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.main.DialogAdapter;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClick;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClickListener;
import com.losextraditables.bu.utils.RemindTask;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.view.adapter.VideoAdapter;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.presenter.VideoListPresenter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.inject.Inject;

public class VideoFragment extends BaseFragment
    implements VideoListPresenter.View {

  @Bind(R.id.videos) GridView videoRecycler;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject @Presenter VideoListPresenter presenter;
  @Inject SessionManager session;

  private VideoAdapter adapter;
  private LinearLayoutManager linearLayoutManager;
  private Timer timer;

  public static VideoFragment newInstance() {
    return new VideoFragment();
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_video;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupToolbar();
    setupAdapter();
    presenter.showVideos(session.getUid());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void setupToolbar() {
    toolbar.setTitle(this.getResources().getString(R.string.title_activity_video));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getActivity().getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(getResources().getColor(R.color.textColorPrimary));
    }
  }

  private void setupAdapter() {
    adapter = new VideoAdapter(getContext(), new OnVideoClickListener() {
      @Override public void onItemLongClick(View view, String url) {
        showRemoveVideoAlert(url);
      }
    }, new OnVideoClick() {
      @Override public void onItemClick(View view, VideoModel videoModel) {
        startActivity(
            WatchVideoActivity.getIntentForPicturesActivity(view.getContext(), videoModel));
      }
    });
    videoRecycler.setAdapter(adapter);
  }

  private void showRemoveVideoAlert(final String url) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    builder.setMessage("Do you want to delete the video?");

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        presenter.removeVideo(session.getUid(), url);
      }
    });

    builder.create().show();
  }

  @Override public void showVideos(List<VideoModel> videoModels) {
    adapter.setVideoList(videoModels);
    adapter.notifyDataSetChanged();
  }

  @Override public void showAddVideoDialog() {
    ArrayList<Integer> instructionPictures = new ArrayList<>();
    instructionPictures.add(R.drawable.instagram_picture);
    instructionPictures.add(R.drawable.instagram_picture_url);

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_add_video, null);
    ViewPager dialogPager = (ViewPager) dialogView.findViewById(R.id.dialog_pager);
    DialogAdapter dialogAdapter = new DialogAdapter(getContext(), instructionPictures);
    dialogPager.setAdapter(dialogAdapter);
    timer = new Timer();
    timer.scheduleAtFixedRate(new RemindTask(getActivity(), dialogPager, 2, timer), 0, 3000);

    final EditText url = (EditText) dialogView.findViewById(R.id.instagram_url);
    url.clearFocus();
    new android.app.AlertDialog.Builder(getActivity()).setView(dialogView)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            timer.cancel();
            hideLoading();
            presenter.saveVideo(url.getText().toString(), session.getUid());
          }
        })
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override public void onCancel(DialogInterface dialogInterface) {
            timer.cancel();
          }
        })
        .create()
        .show();
  }

  @Override public void refresh() {
    presenter.showVideos(session.getUid());
  }

  @Override public void hideLoading() {
    /*no-op*/
  }

  @Override public void showLoading() {
    /*no-op*/
  }

  @OnClick(R.id.fab) void onFabClick() {
    presenter.onAddVideoClick();
  }

  @Override public void showGenericError() {
    /* no-op */
  }

  @Override public void showConnectionError() {
    /* no-op */
  }

  @Override public void scrollListToTop() {
    videoRecycler.smoothScrollToPosition(0);
  }

  @Override public void onResume() {
    super.onResume();
    if (timer != null) {
      timer.cancel();
    }
  }
}
