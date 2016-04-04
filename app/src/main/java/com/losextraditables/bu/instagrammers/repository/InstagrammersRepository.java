package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.repository.datasource.InstagrammersApiDatasource;

import java.util.List;

import javax.inject.Inject;

public class InstagrammersRepository {

    private final InstagrammersApiDatasource apiDatasource;

    @Inject
    public InstagrammersRepository(InstagrammersApiDatasource apiDatasource) {
        this.apiDatasource = apiDatasource;
    }

    public List<Instagrammer> getInstagrammers() throws Exception {
        return apiDatasource.getInstagrammers();
    }

    public List<SearchedInstagrammer> searchInstagrammers(String query, String accessToken) {
        return apiDatasource.searchIntagrammers(query, accessToken);
    }
}
