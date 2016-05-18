package com.losextraditables.bu.instagrammers.repository.service;

import android.util.Log;
import javax.inject.Inject;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class InstagramServiceGenerator {

  public static final String ENDPOINT = "https://api.instagram.com/v1";

  @Inject public InstagramServiceGenerator() {
  }

  public InstagramApiService createInstagramService() {
    RestAdapter adapter = new RestAdapter.Builder() //
        .setEndpoint(ENDPOINT) //
        .setLog(new RestAdapter.Log() {
          @Override
          public void log(String message) {
            Log.d("Retrofit", message);
          }
        }).setErrorHandler(new ErrorHandler() {
          @Override
          public Throwable handleError(RetrofitError cause) {
            Log.d("error", cause.getUrl());
            Log.d("error", cause.getMessage());
            return null;
          }
        })
        .build();
    return adapter.create(InstagramApiService.class);
  }
}
