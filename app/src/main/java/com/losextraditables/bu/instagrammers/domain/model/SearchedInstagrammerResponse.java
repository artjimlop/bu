package com.losextraditables.bu.instagrammers.domain.model;

import java.util.List;

public class SearchedInstagrammerResponse {

  private Meta meta;
  private List<SearchedInstagrammer> data;

  public List<SearchedInstagrammer> getData() {
    return data;
  }

  public void setData(List<SearchedInstagrammer> data) {
    this.data = data;
  }

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }
}
