package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.FollowedInstagrammersRepository;

import java.util.List;

import javax.inject.Inject;

public class GetFollowedInstagrammersUseCase extends RosieUseCase {

    private final FollowedInstagrammersRepository followedInstagrammersRepository;

    @Inject
    public GetFollowedInstagrammersUseCase(FollowedInstagrammersRepository followedInstagrammersRepository) {
        this.followedInstagrammersRepository = followedInstagrammersRepository;
    }

    @UseCase
    public void getInstagrammers() throws Exception {
        notifySuccess(getInstagrammersFromRepository());
    }

    protected List<Instagrammer> getInstagrammersFromRepository() throws Exception {
        return followedInstagrammersRepository.getInstagrammers();
    }
}
