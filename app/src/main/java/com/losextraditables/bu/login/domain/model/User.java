package com.losextraditables.bu.login.domain.model;

import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import java.util.List;

public class User {

  private String username;
  private List<String> pictures;
  private List<Instagrammer> instagrammers;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getPictures() {
    return pictures;
  }

  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }

  public List<Instagrammer> getInstagrammers() {
    return instagrammers;
  }

  public void setInstagrammers(
      List<Instagrammer> instagrammers) {
    this.instagrammers = instagrammers;
  }
}
