package com.losextraditables.bu.instagrammers.view.model.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.view.model.SearchedInstagrammerModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class SearchedInstagrammerModelMapper
    extends Mapper<SearchedInstagrammer, SearchedInstagrammerModel> {

  @Inject
  public SearchedInstagrammerModelMapper() {
  }

  @Override
  public SearchedInstagrammerModel map(SearchedInstagrammer value) {
    SearchedInstagrammerModel searchedInstagrammerModel = new SearchedInstagrammerModel();
    searchedInstagrammerModel.setBio(value.getBio());
    searchedInstagrammerModel.setFullName(value.getFullname());
    searchedInstagrammerModel.setId(value.getId());
    searchedInstagrammerModel.setProfilePicture(value.getProfilePicture());
    searchedInstagrammerModel.setUserName(value.getUsername());
    searchedInstagrammerModel.setWebsite(value.getWebsite());
    return searchedInstagrammerModel;
  }

  @Override
  public SearchedInstagrammer reverseMap(SearchedInstagrammerModel value) {
    SearchedInstagrammer searchedInstagrammer = new SearchedInstagrammer();
    searchedInstagrammer.setWebsite(value.getWebsite());
    searchedInstagrammer.setId(value.getId());
    searchedInstagrammer.setFullname(value.getFullName());
    searchedInstagrammer.setProfilePicture(value.getProfilePicture());
    searchedInstagrammer.setUsername(value.getUserName());
    return searchedInstagrammer;
  }

  public List<SearchedInstagrammerModel> map(List<SearchedInstagrammer> searchedInstagrammers) {
    List<SearchedInstagrammerModel> models = new ArrayList<>();
    for (SearchedInstagrammer searchedInstagrammer : searchedInstagrammers) {
      models.add(map(searchedInstagrammer));
    }
    return models;
  }
}
