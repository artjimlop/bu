package com.losextraditables.bu.instagrammers.view.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.fragment.BaseFragment;
import com.losextraditables.bu.bottombar.view.BottomBarPresenter;
import com.losextraditables.bu.instagrammers.view.adapter.InstagrammersAdapter;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.InstagrammersListPresenter;
import com.losextraditables.bu.main.DialogAdapter;
import com.losextraditables.bu.main.MainTabbedActivity;
import com.losextraditables.bu.pictures.view.adapter.OnInstagrammerClickListener;
import com.losextraditables.bu.utils.RemindTask;
import com.losextraditables.bu.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.inject.Inject;

public class InstagrammersFragment extends BaseFragment
    implements InstagrammersListPresenter.View {

  private static final int ANIMATION_DURATION = 500;
  @Bind(R.id.instagrammers_list)
  RecyclerView instagrammersList;

  @Bind(R.id.instagrammers_progress) ProgressBar progressBar;

  @Inject
  @Presenter
  InstagrammersListPresenter instagrammersListPresenter;

  @Inject
  @Presenter
  BottomBarPresenter bottomBarPresenter;

  @Inject SessionManager session;

  private InstagrammersAdapter adapter;
  private LinearLayoutManager linearLayoutManager;
  private View sharedImage;
  private Timer timer;

  public static InstagrammersFragment newInstance() {
    return new InstagrammersFragment();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_instagrammers;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupAdapter();
    ((MainTabbedActivity) getActivity()).setUpToolbar(false,
        this.getResources().getString(R.string.instagrammers_activity));
    instagrammersListPresenter.showInstagrammers(session.getUid());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void setupAdapter() {
    adapter =
        new InstagrammersAdapter(getContext(), new InstagrammersListPresenter.ItemClickListener() {
          @Override public void onItemClick(View view, InstagrammerModel instagrammerModel) {
            sharedImage = view.findViewById(R.id.instagrammer_avatar);
            instagrammersListPresenter.goToInstagrammerDetail(instagrammerModel);
          }
        }, new OnInstagrammerClickListener() {
          @Override public void onItemLongClick(View view, String username) {
            showRemoveInstagrammerAlert(username);
          }
        });
    instagrammersList.setAdapter(adapter);
    linearLayoutManager = new LinearLayoutManager(getContext());
    instagrammersList.setLayoutManager(linearLayoutManager);
  }

  private void showRemoveInstagrammerAlert(final String usename) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    builder.setMessage(R.string.delete_instagrammer_question);

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        instagrammersListPresenter.removeInstagrammer(session.getUid(), usename);
      }
    });

    builder.create().show();
  }

  @Override
  public void showInstagrammers(List<InstagrammerModel> instagrammerModels) {
    adapter.setUsers(instagrammerModels);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void goToInstagrammerDetail(InstagrammerModel instagrammerModel) {
    InstagrammerDetailActivity.init(getActivity(), sharedImage, instagrammerModel);
  }

  @Override public void showSaveInstagrammerDialog() {
    ArrayList<Integer> instructionPictures = new ArrayList<>();
    instructionPictures.add(R.drawable.user_profile);
    instructionPictures.add(R.drawable.user_profile_url);

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_add_instagrammer, null);
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
            instagrammersListPresenter.saveUser(url.getText().toString(), session.getUid());
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

  @Override
  public void hideLoading() {
    instagrammersList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showLoading() {
    instagrammersList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.fab) void onFabClick() {
    showSaveInstagrammerDialog();
  }

  @Override public void showGenericError() {
    /* no-op */
  }

  @Override public void showConnectionError() {
    /* no-op */
  }

  @Override public void scrollListToTop() {
    instagrammersList.smoothScrollToPosition(0);
  }
}
