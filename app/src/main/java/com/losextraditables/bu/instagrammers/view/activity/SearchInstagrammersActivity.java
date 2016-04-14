package com.losextraditables.bu.instagrammers.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.rosie.view.Presenter;
import com.losextraditables.bu.R;
import com.losextraditables.bu.base.view.activity.BuAppCompatActivity;
import com.losextraditables.bu.instagrammers.InstagrammersListModule;
import com.losextraditables.bu.instagrammers.view.adapter.SearchedInstagrammersAdapter;
import com.losextraditables.bu.instagrammers.view.model.SearchedInstagrammerModel;
import com.losextraditables.bu.instagrammers.view.presenter.SearchInstagrammersPresenter;
import com.losextraditables.bu.login.activity.LoginActivity;
import com.losextraditables.bu.utils.InstagramSession;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchInstagrammersActivity extends BuAppCompatActivity implements SearchInstagrammersPresenter.View {

    @Bind(R.id.find_instagrammers_list)
    RecyclerView resultsListView;
    @Bind(R.id.find_instagrammers_empty)
    TextView emptyOrErrorView;
    @Bind(R.id.find_instagrammers_progress)
    ProgressBar progressBar;

    @Inject @Presenter
    SearchInstagrammersPresenter presenter;

    @Inject
    InstagramSession session;

    private SearchedInstagrammersAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private SearchView searchView;

    protected List<Object> getActivityScopeModules() {
        return Collections.singletonList((Object) new InstagrammersListModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupActionBar();
        setupViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_instagrammers;
    }

    @Override
    protected void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setupViews() {
        if (adapter == null) {
            adapter = new SearchedInstagrammersAdapter(this);
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        resultsListView.setAdapter(adapter);
        resultsListView.setLayoutManager(gridLayoutManager);
    }

    private void setupActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        createSearchView(searchItem);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.application_background));
        return true;
    }

    private void createSearchView(MenuItem searchItem) {
        final String accessToken = session.getAccessToken(this);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String queryText) {
                presenter.search(queryText, accessToken);
                return true;
            }

            @Override public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.find_instagrammers));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
    }


    @Override
    public void renderInstagrammers(List<SearchedInstagrammerModel> instagrammers) {
        adapter.setUsers(instagrammers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setCurrentQuery(String query) {

    }

    @Override
    public void hideKeyboard() {
        //TODO
    }

    @Override
    public void showContent() {
        resultsListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        resultsListView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmpty() {
        emptyOrErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        emptyOrErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGenericError() {
        //TODO
    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }
}
