package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;

import java.util.List;

import javax.inject.Inject;

public class GetFollowedInstagrammersUseCase extends RosieUseCase {

    private final InstagrammersRepository instagrammersRepository;

    @Inject
    public GetFollowedInstagrammersUseCase(InstagrammersRepository instagrammersRepository) {
        this.instagrammersRepository = instagrammersRepository;
    }

    @UseCase
    public void getInstagrammers() throws Exception {
        notifySuccess(getInstagrammersFromRepository());
    }

    protected List<Instagrammer> getInstagrammersFromRepository() throws Exception {
        return instagrammersRepository.getInstagrammers();
    }
}
