package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class FollowedInstagrammersFakeDatasource implements FollowedInstagrammersDatasource {

    @Inject
    public FollowedInstagrammersFakeDatasource() {
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
