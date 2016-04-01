package com.losextraditables.bu.instagrammers.repository.datasource;

import android.util.Log;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammerResponse;
import com.losextraditables.bu.instagrammers.repository.service.InstagramApiService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class FollowedInstagrammersApiDatasource implements FollowedInstagrammersDatasource {

    @Inject
    public FollowedInstagrammersApiDatasource() {
    }

    @Override
    public List<Instagrammer> getInstagrammers() {
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
        RestAdapter adapter = new RestAdapter.Builder() //
                .setEndpoint("https://api.instagram.com/v1") //
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Log.d("Retrofit", message);
                    }
                }).setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
                        Log.d("error", cause.getUrl());
                        Log.d("error", cause.getMessage());
                        return null;
                    }
                })
                .build();
        InstagramApiService service = adapter.create(InstagramApiService.class);
        SearchedInstagrammerResponse searchedInstagrammerResponse = service.searchInstagrammers(query, accessToken);
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
