package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.BuildConfig;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersApiDatasource;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersDatasource;
import com.losextraditables.bu.instagrammers.repository.datasource.FollowedInstagrammersFakeDatasource;

import javax.inject.Inject;

class InstagrammersDataSourceFactory {

  @Inject public InstagrammersDataSourceFactory() {
  }

  FollowedInstagrammersDatasource createDataSource() {
    if (hasKeys()) {
      return new FollowedInstagrammersApiDatasource();
    } else {
      return new FollowedInstagrammersFakeDatasource();
    }
  }

  private boolean hasKeys() {
    return BuildConfig.CLIENT_ID != null && BuildConfig.CLIENT_SECRET != null;
  }

}
