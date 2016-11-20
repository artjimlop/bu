package com.losextraditables.bu.pictures.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.pictures.model.LatestItemModel;
import com.losextraditables.bu.pictures.view.adapter.ItemClickListener;
import com.losextraditables.bu.pictures.view.adapter.LatestAdapter;
import com.losextraditables.bu.pictures.view.adapter.OnItemLongClickListener;
import com.losextraditables.bu.pictures.view.presenter.LatestPresenter;
import com.losextraditables.bu.utils.SessionManager;
import com.losextraditables.bu.videos.view.activity.WatchVideoActivity;
import java.util.List;
import javax.inject.Inject;

public class LatestFragment extends BaseFragment implements LatestPresenter.View {

  @Bind(R.id.latest_list)
  GridView latestList;
  @Bind(R.id.latest_progress)
  ProgressBar progressBar;

  @Inject
  @Presenter
  LatestPresenter latestPresenter;

  @Inject
  SessionManager session;

  private LatestAdapter adapter;

  public LatestFragment() {
    // Required empty public constructor
  }

  public static LatestFragment newInstance() {
    return new LatestFragment();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_latest;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupToolbar();
    latestPresenter.loadLatest();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void setupToolbar() {
    ((MainTabbedActivity) getActivity()).setUpToolbar(false,
        this.getResources().getString(R.string.latest_fragment));
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void scrollListToTop() {
    latestList.smoothScrollToPosition(0);
  }

  @Override
  public void showSavedPictures(List<LatestItemModel> pictures) {
    adapter = new LatestAdapter(getContext(), pictures, new ItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {
        goToSavedPictureActivity(view, position);
      }
    }, new OnItemLongClickListener() {
      @Override
      public void onItemLongClick(View view, String url) {
        //showRemovePictureAlert(url);
      }
    });
    latestList.setAdapter(adapter);
  }

  @Override
  public void showRetry() {
    Snackbar.make(latestList, R.string.nothing_found, Snackbar.LENGTH_LONG)
        .setAction(R.string.retry, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            latestPresenter.loadLatest();
          }
        })
        .show();
  }

  private void goToSavedPictureActivity(View view, int position) {
    LatestItemModel item = adapter.getItem(position);
    if (item.getHasPicture()) {
      PictureActivity.init(getActivity(), null, item.getPicture().getUrl());
      getActivity().overridePendingTransition(R.anim.detail_activity_fade_in,
          R.anim.detail_activity_fade_out);
    } else {
      startActivity(
          WatchVideoActivity.getIntentForPicturesActivity(view.getContext(), item.getVideo()));
      getActivity().overridePendingTransition(R.anim.detail_activity_fade_in,
          R.anim.detail_activity_fade_out);
    }
  }

  @Override
  public void showGenericError() {

  }

  @Override
  public void showConnectionError() {

  }

  @Override
  public void hideLoading() {
    if (latestList != null && progressBar != null) {
      latestList.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void showLoading() {
    latestList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }
}
