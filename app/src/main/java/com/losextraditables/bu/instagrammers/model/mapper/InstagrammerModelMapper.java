package com.losextraditables.bu.instagrammers.model.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.model.InstagrammerModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InstagrammerModelMapper extends Mapper<Instagrammer, InstagrammerModel> {

    @Inject
    public InstagrammerModelMapper() {
    }

    @Override
    public InstagrammerModel map(Instagrammer value) {
        InstagrammerModel instagrammerModel = new InstagrammerModel();
        instagrammerModel.setBio(value.getBio());
        instagrammerModel.setFullName(value.getFullName());
        instagrammerModel.setProfilePicture(value.getProfilePicture());
        instagrammerModel.setUserId(value.getUserId());
        instagrammerModel.setWebsite(value.getWebsite());
        instagrammerModel.setUserName(value.getUserName());
        return instagrammerModel;
    }

    @Override
    public Instagrammer reverseMap(InstagrammerModel value) {
        Instagrammer instagrammer = new Instagrammer();
        instagrammer.setUserName(instagrammer.getUserName());
        instagrammer.setWebsite(instagrammer.getWebsite());
        instagrammer.setUserId(instagrammer.getUserId());
        instagrammer.setProfilePicture(instagrammer.getProfilePicture());
        instagrammer.setFullName(instagrammer.getFullName());
        instagrammer.setBio(instagrammer.getBio());
        return instagrammer;
    }

    public List<InstagrammerModel> mapList(List<Instagrammer> instagrammers) {
        List<InstagrammerModel> instagrammerModels = new ArrayList<>();
        for (Instagrammer instagrammer : instagrammers) {
            instagrammerModels.add(map(instagrammer));
        }
        return instagrammerModels;
    }
}
