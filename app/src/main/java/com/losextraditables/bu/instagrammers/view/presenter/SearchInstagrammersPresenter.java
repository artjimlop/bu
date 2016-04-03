package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.SearchInstagrammersUseCase;
import com.losextraditables.bu.instagrammers.view.model.SearchedInstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.SearchedInstagrammerModelMapper;

import java.util.List;

import javax.inject.Inject;

public class SearchInstagrammersPresenter extends BuPresenter<SearchInstagrammersPresenter.View> {

    private final SearchInstagrammersUseCase searchInstagrammersUseCase;
    private final SearchedInstagrammerModelMapper searchedInstagrammerModelMapper;

    @Inject public SearchInstagrammersPresenter(UseCaseHandler useCaseHandler, SearchInstagrammersUseCase searchInstagrammersUseCase, SearchedInstagrammerModelMapper searchedInstagrammerModelMapper) {
        super(useCaseHandler);
        this.searchInstagrammersUseCase = searchInstagrammersUseCase;
        this.searchedInstagrammerModelMapper = searchedInstagrammerModelMapper;
    }

    public void search(String query, String accessToken) {
        getView().hideEmpty();
        getView().hideContent();
        getView().hideKeyboard();
        getView().showLoading();
        getView().setCurrentQuery(query);
        createUseCaseCall(searchInstagrammersUseCase).args(query, accessToken).onSuccess(new OnSuccessCallback() {
            @Success
            public void onInstagrammersFound(List<SearchedInstagrammer> instagrammers) {
                showInstagrammersInView(instagrammers);
            }
        }).onError(new OnErrorCallback() {
            @Override
            public boolean onError(Error error) {
                showErrorInView();
                return false;
            }
        }).execute();
    }

    protected void showErrorInView() {
        getView().showEmpty();
        getView().showConnectionError();
    }

    protected void showInstagrammersInView(List<SearchedInstagrammer> instagrammers) {
        List<SearchedInstagrammerModel> models = searchedInstagrammerModelMapper.map(instagrammers);
        getView().showContent();
        getView().renderInstagrammers(models);
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
