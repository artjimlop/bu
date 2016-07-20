package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.domain.usecase.GetProfilePicturesUseCase;
import java.util.List;
import javax.inject.Inject;

public class InstagrammerDetailPresenter extends BuPresenter<InstagrammerDetailPresenter.View> {

  private final GetProfilePicturesUseCase getProfilePicturesUseCase;

  @Inject public InstagrammerDetailPresenter(UseCaseHandler useCaseHandler,
      GetProfilePicturesUseCase getProfilePicturesUseCase) {
    super(useCaseHandler);
    this.getProfilePicturesUseCase = getProfilePicturesUseCase;
  }

  public void getProfilePictures(String profileUrl) {
    getView().showLoading();
    createUseCaseCall(getProfilePicturesUseCase).args(profileUrl)
        .onSuccess(new OnSuccessCallback() {
          @Success
          public void onPcitruesFound(List<String> pictures) {
            getView().renderProfilePictures(pictures);
          }
        })
        .onError(new OnErrorCallback() {
          @Override
          public boolean onError(Error error) {
            showErrorInView();
            return false;
          }
        })
        .execute();
  }

  protected void showErrorInView() {
    getView().showEmpty();
    getView().showConnectionError();
  }

  public interface View extends BuPresenter.View {
    void renderProfilePictures(List<String> pictures);

    void showEmpty();
  }
}
