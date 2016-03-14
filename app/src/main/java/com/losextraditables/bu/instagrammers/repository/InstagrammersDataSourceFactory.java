package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersApiDatasource;

import javax.inject.Inject;

class InstagrammersDataSourceFactory {

  @Inject public InstagrammersDataSourceFactory() {
  }

  FollowedInstagrammersApiDatasource createDataSource() {
    return new FollowedInstagrammersApiDatasource();
  }

}
