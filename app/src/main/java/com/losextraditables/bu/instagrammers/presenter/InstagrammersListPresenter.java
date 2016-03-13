package com.losextraditables.bu.instagrammers.presenter;

import android.support.annotation.NonNull;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.losextraditables.bu.base.view.presenter.BuPresenter;
import com.losextraditables.bu.instagrammers.model.UserModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class InstagrammersListPresenter extends BuPresenter<InstagrammersListPresenter.View> {

    @Inject public InstagrammersListPresenter(UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
    }

    public void initialize() {
    }

    public void showMockedUsers() {
        List<UserModel> userModels = createFakeUserList();
        getView().showMockedUserList(userModels);
    }

    @NonNull
    private List<UserModel> createFakeUserList() {
        UserModel userModel = new UserModel();
        userModel.setBio("bio");
        userModel.setFullName("fullname");
        userModel.setUserName("username");
        userModel.setProfilePicture("http://developer.android.com/assets/images/android_logo@2x.png");
        userModel.setUserId("userId");
        userModel.setWebsite("website");
        return Arrays.asList(userModel);
    }

    public interface View extends BuPresenter.View {
        void showMockedUserList(List<UserModel> userModels);
    }

}
