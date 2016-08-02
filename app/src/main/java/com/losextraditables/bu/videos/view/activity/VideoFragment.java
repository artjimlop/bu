package com.losextraditables.bu.videos.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.pictures.view.adapter.OnVideoClickListener;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.view.adapter.VideoAdapter;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.presenter.VideoListPresenter;
import java.util.List;
import javax.inject.Inject;

public class VideoFragment extends BaseFragment
    implements VideoListPresenter.View {

  @Bind(R.id.videos) RecyclerView videoRecycler;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject @Presenter VideoListPresenter presenter;
  @Inject SessionManager session;

  private VideoAdapter adapter;
  private LinearLayoutManager linearLayoutManager;

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
  }

  private void setupAdapter() {
    adapter = new VideoAdapter(getContext(), new OnVideoClickListener() {
      @Override public void onItemLongClick(View view, String url) {
        showRemoveVideoAlert(url);
      }
    });
    videoRecycler.setAdapter(adapter);
    linearLayoutManager = new LinearLayoutManager(getContext());
    videoRecycler.setLayoutManager(linearLayoutManager);
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
    final Context context = getContext();
    AlertDialog.Builder builder = new AlertDialog.Builder(context);

    builder.setMessage("Insert video's url here").setTitle("Save video");

    final EditText input = new EditText(context);

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        presenter.saveVideo(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
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
}
