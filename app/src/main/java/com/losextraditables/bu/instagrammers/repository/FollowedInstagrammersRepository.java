package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersApiDatasource;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersDatasource;

import java.util.List;

import javax.inject.Inject;

public class FollowedInstagrammersRepository{

    private final FollowedInstagrammersApiDatasource apiDatasource;

    @Inject
    public FollowedInstagrammersRepository(FollowedInstagrammersApiDatasource apiDatasource) {
        this.apiDatasource = apiDatasource;
    }

    public List<Instagrammer> getInstagrammers() throws Exception {
        return apiDatasource.getInstagrammers();
    }

    public List<SearchedInstagrammer> searchInstagrammers(String query, String accessToken) {
        return apiDatasource.searchIntagrammers(query, accessToken);
    }
}
