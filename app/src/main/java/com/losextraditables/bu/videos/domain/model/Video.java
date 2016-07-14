package com.losextraditables.bu.videos.domain.model;

import lombok.Data;

@Data public class Video {
  private String title;
  private String image;
  private String url;

  public String getUrl() {
    return url;
  }
}

