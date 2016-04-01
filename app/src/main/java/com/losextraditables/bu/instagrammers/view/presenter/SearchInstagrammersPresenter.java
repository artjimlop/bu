package com.losextraditables.bu.instagrammers.view.presenter;

import android.util.Log;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.SearchInstagrammers;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.SearchedInstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.InstagrammerModelMapper;
import com.losextraditables.bu.instagrammers.view.model.mapper.SearchedInstagrammerModelMapper;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class SearchInstagrammersPresenter extends BuPresenter<SearchInstagrammersPresenter.View> {

    private final SearchInstagrammers searchInstagrammers;
    private final InstagrammerModelMapper instagrammerModelMapper;
    private final SearchedInstagrammerModelMapper searchedInstagrammerModelMapper;
    private String query;

    @Inject
    public SearchInstagrammersPresenter(UseCaseHandler useCaseHandler, SearchInstagrammers searchInstagrammers, InstagrammerModelMapper instagrammerModelMapper, SearchedInstagrammerModelMapper searchedInstagrammerModelMapper) {
        super(useCaseHandler);
        this.searchInstagrammers = searchInstagrammers;
        this.instagrammerModelMapper = instagrammerModelMapper;
        this.searchedInstagrammerModelMapper = searchedInstagrammerModelMapper;
    }

    public void search(String query, String accessToken) {
        this.query = query;
        getView().hideEmpty();
        getView().hideContent();
        getView().hideKeyboard();
        getView().showLoading();
        getView().setCurrentQuery(query);
        createUseCaseCall(searchInstagrammers).args(query, accessToken).onSuccess(new OnSuccessCallback() {
            @Success
            public void onInstagrammersFound(List<SearchedInstagrammer> instagrammers) {
                List<SearchedInstagrammerModel> models = searchedInstagrammerModelMapper.map(instagrammers);
                Log.d("instagrammers", instagrammers.get(0).toString());
                getView().showContent();
                getView().renderInstagrammers(models);
            }
        }).onError(new OnErrorCallback() {
            @Override
            public boolean onError(Error error) {
                getView().showEmpty();
                return false;
            }
        }).execute();
    }

    public interface View extends BuPresenter.View {
        void renderInstagrammers(List<SearchedInstagrammerModel> instagrammers);

        void setCurrentQuery(String query);

        void hideKeyboard();

        void showContent();

        void hideContent();

        void hideEmpty();

        void showEmpty();
    }
}
