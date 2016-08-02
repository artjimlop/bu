package com.losextraditables.bu.instagrammers.view.activity;

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
import com.losextraditables.bu.pictures.view.adapter.OnInstagrammerClickListener;
import com.losextraditables.bu.utils.SessionManager;
import java.util.List;
import javax.inject.Inject;

public class InstagrammersFragment extends BaseFragment
    implements InstagrammersListPresenter.View {

  private static final int ANIMATION_DURATION = 500;
  @Bind(R.id.instagrammers_list)
  RecyclerView instagrammersList;
  @Bind(R.id.toolbar) Toolbar toolbar;

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
    toolbar.setTitle(this.getResources().getString(R.string.instagrammers_activity));
    instagrammersListPresenter.showInstagrammers(session.getUid());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void setupAdapter() {
    adapter = new InstagrammersAdapter(getContext(), new InstagrammersListPresenter.ItemClickListener() {
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

    builder.setMessage("Do you want to delete the instagrammer?");

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
    final Context context = getContext();
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage("Insert instagrammers's url here")
        .setTitle("Save");

    final EditText input = new EditText(context);
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        hideLoading();
        bottomBarPresenter.saveUser(input.getText().toString(), session.getUid());
      }
    });

    builder.create().show();
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
    bottomBarPresenter.saveInstagrammerClicked();
  }

  @Override public void showGenericError() {
    /* no-op */
  }

  @Override public void showConnectionError() {
    /* no-op */
  }
}
