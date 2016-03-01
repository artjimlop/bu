package com.losextraditables.bu.base.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import com.losextraditables.bu.base.view.error.ConnectionError;

public class BuPresenter<T extends BuPresenter.View> extends RosiePresenterWithLoading<T> {

    public BuPresenter(UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
        registerOnErrorCallback(onErrorCallback);
    }

    private final OnErrorCallback onErrorCallback = new OnErrorCallback() {
        @Override
        public boolean onError(Error error) {
            if (error instanceof ConnectionError) {
                getView().showConnectionError();
            } else {
                getView().showGenericError();
            }
            return true;
        }
    };

    public interface View extends RosiePresenterWithLoading.View {
        void showGenericError();

        void showConnectionError();
    }
}