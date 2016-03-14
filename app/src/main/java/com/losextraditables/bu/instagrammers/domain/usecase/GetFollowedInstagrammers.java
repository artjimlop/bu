package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.repository.FollowedInstagrammersRepository;

import javax.inject.Inject;

public class GetFollowedInstagrammers extends RosieUseCase {

    private final FollowedInstagrammersRepository followedInstagrammersRepository;

    @Inject
    public GetFollowedInstagrammers(FollowedInstagrammersRepository followedInstagrammersRepository) {
        this.followedInstagrammersRepository = followedInstagrammersRepository;
    }

    @UseCase
    public void getInsagrammets() throws Exception {
        notifySuccess(followedInstagrammersRepository.getInstagrammers());
    }
}
