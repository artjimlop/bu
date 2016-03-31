package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.SearchInstagrammers;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.InstagrammerModelMapper;

import java.util.List;

import javax.inject.Inject;

public class SearchInstagrammersPresenter extends BuPresenter<SearchInstagrammersPresenter.View> {

    private final SearchInstagrammers searchInstagrammers;
    private final InstagrammerModelMapper mapper;
    private String query;

    @Inject
    public SearchInstagrammersPresenter(UseCaseHandler useCaseHandler, SearchInstagrammers searchInstagrammers, InstagrammerModelMapper mapper) {
        super(useCaseHandler);
        this.searchInstagrammers = searchInstagrammers;
        this.mapper = mapper;
    }

    public void search(String query) {
        this.query = query;
        getView().hideEmpty();
        getView().hideContent();
        getView().hideKeyboard();
        getView().showLoading();
        getView().setCurrentQuery(query);
        createUseCaseCall(searchInstagrammers).onSuccess(new OnSuccessCallback() {
            @Success
            public void onInstagrammersFound(List<Instagrammer> instagrammers) {
                List<InstagrammerModel> models = mapper.mapList(instagrammers);
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
        void renderInstagrammers(List<InstagrammerModel> instagrammers);

        void setCurrentQuery(String query);

        void hideKeyboard();

        void showContent();

        void hideContent();

        void hideEmpty();

        void showEmpty();
    }
}
