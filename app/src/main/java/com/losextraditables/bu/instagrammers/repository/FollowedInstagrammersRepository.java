package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersDatasource;

import java.util.List;

import javax.inject.Inject;

public class FollowedInstagrammersRepository{

    private final FollowedInstagrammersDatasource followedInstagrammersDatasource;

    @Inject
    public FollowedInstagrammersRepository(InstagrammersDataSourceFactory factory) {
        this.followedInstagrammersDatasource = factory.createDataSource();
    }

    public List<Instagrammer> getInstagrammers() throws Exception {
        return followedInstagrammersDatasource.getInstagrammers();
    }

}
