package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.GetFollowedInstagrammersUseCase;
import com.losextraditables.bu.instagrammers.view.model.InstagrammerModel;
import com.losextraditables.bu.instagrammers.view.model.mapper.InstagrammerModelMapper;

import java.util.List;

import javax.inject.Inject;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

    private final GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase;
    private final InstagrammerModelMapper mapper;

    @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler, GetFollowedInstagrammersUseCase getFollowedInstagrammersUseCase, InstagrammerModelMapper mapper) {
        super(useCaseHandler);
        this.getFollowedInstagrammersUseCase = getFollowedInstagrammersUseCase;
        this.mapper = mapper;
    }

    public void initialize() {
    }

    public void showMockedInstagrammers() {
        createUseCaseCall(getFollowedInstagrammersUseCase)
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

    public void goToInstagrammerDetail(InstagrammerModel instagrammerModel){
        getView().goToInstagrammerDetail(instagrammerModel);
    }

    public interface View extends BuPresenter.View {
        void showMockedInstagrammers(List<InstagrammerModel> instagrammerModels);

        void goToInstagrammerDetail(InstagrammerModel instagrammerModel);
    }

    public interface ItemClickListener {
        void onItemClick(android.view.View view, InstagrammerModel instagrammerModel);
    }

}
