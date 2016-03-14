package com.losextraditables.bu.instagrammers.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.GetFollowedInstagrammers;
import com.losextraditables.bu.instagrammers.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.model.mapper.InstagrammerModelMapper;

import java.util.List;

import javax.inject.Inject;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

    private final GetFollowedInstagrammers getFollowedInstagrammers;
    private final InstagrammerModelMapper mapper;

    @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler, GetFollowedInstagrammers getFollowedInstagrammers, InstagrammerModelMapper mapper) {
        super(useCaseHandler);
        this.getFollowedInstagrammers = getFollowedInstagrammers;
        this.mapper = mapper;
    }

    public void initialize() {
    }

    public void showMockedInstagrammers() {
        createUseCaseCall(getFollowedInstagrammers)
                .onSuccess(new OnSuccessCallback() {
                    @Success
                    public void onInstagrammersLoaded(List<Instagrammer> instagrammers) {
                        List<InstagrammerModel> instagrammerModels = mapper.mapList(instagrammers);
                        getView().showMockedInstagrammers(instagrammerModels);
                    }
                })
                .onError(new OnErrorCallback() {
                    @Override public boolean onError(Error error) {
                        getView().hideLoading();
                        return false;
                    }
                })
                .execute();
    }

    public interface View extends BuPresenter.View {
        void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels);
    }

}
