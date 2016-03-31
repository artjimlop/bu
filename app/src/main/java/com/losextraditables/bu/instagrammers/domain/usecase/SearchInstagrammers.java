package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;

import java.util.Collections;

import javax.inject.Inject;

public class SearchInstagrammers extends RosieUseCase {

    @Inject
    public SearchInstagrammers() {
    }

    @UseCase
    public void getInstagrammers() throws Exception {
        //TODO call remote
        notifySuccess(Collections.emptyList());
    }
}
