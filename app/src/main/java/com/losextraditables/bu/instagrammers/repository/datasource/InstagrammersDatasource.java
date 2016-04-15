package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import java.util.List;

public interface InstagrammersDatasource
    extends PaginatedReadableDataSource<Integer, List<Instagrammer>> {
  List<Instagrammer> getInstagrammers();

  List<SearchedInstagrammer> searchIntagrammers(String query, String accessToken);
}
