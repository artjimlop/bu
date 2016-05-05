package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import java.util.List;
import rx.Observable;

public interface InstagrammersDatasource
    extends PaginatedReadableDataSource<Integer, List<Instagrammer>> {
  Observable<List<Instagrammer>> getInstagrammers(String uid);

  List<SearchedInstagrammer> searchIntagrammers(String query, String accessToken);

  Observable<Instagrammer> getInstagrammerFromScrap(String url);
}
