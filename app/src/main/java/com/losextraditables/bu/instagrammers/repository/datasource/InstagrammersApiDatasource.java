package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammerResponse;
import com.losextraditables.bu.instagrammers.repository.service.InstagramApiService;
import com.losextraditables.bu.instagrammers.repository.service.InstagramServiceGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class InstagrammersApiDatasource implements InstagrammersDatasource {

    private final InstagramServiceGenerator  serviceGenerator;

    @Inject
    public InstagrammersApiDatasource(InstagramServiceGenerator serviceGenerator) {
        this.serviceGenerator = serviceGenerator;
    }

    @Override
    public List<Instagrammer> getInstagrammers() {
        //TODO: this method it's still a fake, it will be implemented when Firebase is added
        Instagrammer instagrammer = new Instagrammer();
        instagrammer.setBio("bio");
        instagrammer.setFullName("fullname");
        instagrammer.setUserName("username");
        instagrammer.setProfilePicture("http://developer.android.com/assets/images/android_logo@2x.png");
        instagrammer.setUserId("userId");
        instagrammer.setWebsite("website");
        instagrammer.setKey("key");
        return Arrays.asList(instagrammer);
    }

    @Override
    public List<SearchedInstagrammer> searchIntagrammers(String query, String accessToken) {
        InstagramApiService instagramService = serviceGenerator.createInstagramService();
        SearchedInstagrammerResponse searchedInstagrammerResponse = instagramService.searchInstagrammers(query, accessToken);
        return searchedInstagrammerResponse.getData();
    }

    @Override
    public PaginatedCollection<List<Instagrammer>> getPage(Page page) throws Exception {
        return null;
    }

    @Override
    public List<Instagrammer> getByKey(Integer key) throws Exception {
        return null;
    }

    @Override
    public Collection<List<Instagrammer>> getAll() throws Exception {
        return null;
    }
}
