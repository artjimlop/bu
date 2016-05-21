package com.losextraditables.bu.instagrammers.repository.service;

import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammerResponse;
import retrofit.http.GET;
import retrofit.http.Query;

public interface InstagramApiService {

  @GET("/users/search/") SearchedInstagrammerResponse searchInstagrammers(@Query("q") String query,
      @Query("access_token") String token);
}
