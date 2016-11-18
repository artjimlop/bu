package com.losextraditables.bu.pictures.view.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.losextraditables.bu.main.DialogAdapter;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.losextraditables.bu.pictures.view.adapter.ItemClickListener;
import com.losextraditables.bu.pictures.view.adapter.OnItemLongClickListener;
import com.losextraditables.bu.pictures.view.adapter.SavedPicturesAdapter;
import com.losextraditables.bu.pictures.view.presenter.PicturesPresenter;
import com.losextraditables.bu.utils.Intents;
import com.losextraditables.bu.utils.RemindTask;
import com.losextraditables.bu.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.inject.Inject;

public class PicturesFragment extends BaseFragment
    implements PicturesPresenter.View {

  @Bind(R.id.pictures_list) GridView picturesList;
  @Bind(R.id.saved_pictures_progress) ProgressBar progressBar;

  @Inject
  @Presenter
  PicturesPresenter picturesPresenter;

  @Inject SessionManager session;

  private SavedPicturesAdapter adapter;
  private Timer timer;

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
    ((MainTabbedActivity) getActivity()).setUpToolbar(false,
        this.getResources().getString(R.string.pictures_activity));
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override public void showPicture(String pictureUrl) {
    startActivity(PictureActivity.getIntentForPicturesActivity(getActivity(), pictureUrl));
  }

  @Override
  public void showRetry() {
    Snackbar.make(picturesList, R.string.nothing_found, Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        picturesPresenter.loadSavedPictures(session.getUid());
      }
    }).show();
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
    ArrayList<Integer> instructionPictures = new ArrayList<>();
    instructionPictures.add(R.drawable.instagram_picture);
    instructionPictures.add(R.drawable.instagram_picture_url);

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_add, null);
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
            picturesPresenter.savePicture(url.getText().toString(), session.getUid());
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

  private void goToSavedPictureActivity(View view, int position) {
    startActivity(
        GalleryActivity.getIntentForPicturesActivity(getContext(), adapter.getImagesUrls(),
            position));
    getActivity().overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }

  @Override
  public void hideLoading() {
    picturesList.setVisibility(View.VISIBLE);
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void showLoading() {
    picturesList.setVisibility(View.GONE);
    if (progressBar != null) {
      progressBar.setVisibility(View.VISIBLE);
    }
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

  @Override public void onPause() {
    super.onPause();
    if (timer != null) {
      timer.cancel();
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_share, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      return true;
    } else if (item.getItemId() == R.id.menu_share) {
      shareApp();
    }
    return super.onOptionsItemSelected(item);
  }

  private void shareApp() {
    String subject = getString(R.string.share_app_subject);
    String message = getString(R.string.share_app_message);
    String url = getString(R.string.share_app_website);

    String sharedText = message + " " + url;

    Intent chooserIntent = ShareCompat.IntentBuilder.from(getActivity())
        .setType("text/plain")
        .setSubject(subject)
        .setText(sharedText)
        .setChooserTitle(R.string.share_app_chooser_title)
        .createChooserIntent();
    Intents.maybeStartActivity(getActivity(), chooserIntent);
  }
}
