package com.losextraditables.bu.pictures.view.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Explode;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.losextraditables.bu.pictures.view.adapter.ItemClickListener;
import com.losextraditables.bu.pictures.view.adapter.OnItemLongClickListener;
import com.losextraditables.bu.pictures.view.adapter.SavedPicturesAdapter;
import com.losextraditables.bu.pictures.view.presenter.PicturesPresenter;
import com.losextraditables.bu.utils.SessionManager;
import java.util.List;
import javax.inject.Inject;

public class PicturesFragment extends BaseFragment
    implements PicturesPresenter.View {

  @Bind(R.id.pictures_list) GridView picturesList;
  @Bind(R.id.saved_pictures_progress) ProgressBar progressBar;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Inject
  @Presenter
  PicturesPresenter picturesPresenter;

  @Inject SessionManager session;

  private SavedPicturesAdapter adapter;

  public static PicturesFragment newInstance() {
    return new PicturesFragment();
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_pictures;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupWindowAnimations();
    setupToolbar();
    picturesPresenter.loadSavedPictures(session.getUid());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void setupToolbar() {
    toolbar.setTitle(this.getResources().getString(R.string.pictures_activity));
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override public void showPicture(String pictureUrl) {
    startActivity(PictureActivity.getIntentForPicturesActivity(getActivity(), pictureUrl));
    getActivity().finish();
  }

  @Override public void showSavedPictures(List<PictureModel> pictures) {
    adapter = new SavedPicturesAdapter(getContext(), pictures, new ItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        goToSavedPictureActivity(view, position);
      }
    }, new OnItemLongClickListener() {
      @Override
      public void onItemLongClick(View view, String url) {
        showRemovePictureAlert(url);
      }
    });
    picturesList.setAdapter(adapter);
  }

  private void showRemovePictureAlert(final String url) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    builder.setMessage("Do you want to delete the picture?");

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        picturesPresenter.removePicure(session.getUid(), url);
      }
    });

    builder.create().show();
  }

  @Override public void showSavePictureDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    builder.setMessage("Insert picture's url here")
        .setTitle("Save picture");

    final EditText input = new EditText(getContext());

    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        hideLoading();
        picturesPresenter.savePicture(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
  }

  private void goToSavedPictureActivity(View view, int position) {
    startActivity(GalleryActivity.getIntentForPicturesActivity(getContext(), adapter.getImagesUrls(), position));
  }

  @Override
  public void hideLoading() {
    picturesList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showLoading() {
    picturesList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void setupWindowAnimations() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getActivity().getWindow().setReenterTransition(new Explode());
      getActivity().getWindow().setExitTransition(new Explode().setDuration(500));
    }
  }

  @OnClick(R.id.fab) void onFabClick() {
    picturesPresenter.onAddPictureClick();
  }

  @Override public void showGenericError() {
    /* no-op */
  }

  @Override public void showConnectionError() {
    /* no-op */
  }

  @Override public void scrollListToTop() {
    picturesList.smoothScrollToPosition(0);
  }
}
