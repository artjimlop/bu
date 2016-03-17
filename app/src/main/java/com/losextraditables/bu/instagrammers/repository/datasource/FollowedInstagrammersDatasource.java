package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;

import java.util.List;

public interface FollowedInstagrammersDatasource extends PaginatedReadableDataSource<Integer, List<Instagrammer>> {
    List<Instagrammer> getInstagrammers();
}
