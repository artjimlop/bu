package com.losextraditables.bu.instagrammers.repository;

import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.repository.datasource.InstagrammersApiDatasource;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class InstagrammersRepository {

  private final InstagrammersApiDatasource apiDatasource;

  @Inject
  public InstagrammersRepository(InstagrammersApiDatasource apiDatasource) {
    this.apiDatasource = apiDatasource;
  }

  public Observable<List<Instagrammer>> getInstagrammers(String uid) throws Exception {
    return apiDatasource.getInstagrammers(uid);
  }

  public List<SearchedInstagrammer> searchInstagrammers(String query, String accessToken) {
    return apiDatasource.searchIntagrammers(query, accessToken);
  }

  public Observable<Instagrammer> getInstagrammer(String url) {
    return apiDatasource.getInstagrammerFromScrap(url);
  }

  public Observable<Void> saveInstagrammer(Instagrammer instagrammer, String uid) {
    return apiDatasource.saveInstagrammer(instagrammer, uid);
  }
}
