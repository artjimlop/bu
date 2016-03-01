package com.losextraditables.bu.instagrammers.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.losextraditables.bu.base.view.presenter.BuPresenter;

import javax.inject.Inject;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

    @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
    }

    public void initialize() {
        getView().showHelloWorld("hello muthafaka");
    }

    public interface View extends BuPresenter.View {
        void showHelloWorld(String hello);
    }

}
