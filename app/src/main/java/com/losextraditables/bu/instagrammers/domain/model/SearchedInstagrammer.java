package com.losextraditables.bu.instagrammers.domain.model;

public class SearchedInstagrammer {

  private String username;
  private String bio;
  private String website;
  private String profile_picture;
  private String full_name;
  private String id;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getProfilePicture() {
    return profile_picture;
  }

  public void setProfilePicture(String profile_picture) {
    this.profile_picture = profile_picture;
  }

  public String getFullname() {
    return full_name;
  }

  public void setFullname(String full_name) {
    this.full_name = full_name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "SearchedInstagrammer{" +
        "username='" + username + '\'' +
        ", bio='" + bio + '\'' +
        ", website='" + website + '\'' +
        ", profile_picture='" + profile_picture + '\'' +
        ", full_name='" + full_name + '\'' +
        ", id='" + id + '\'' +
        '}';
  }
}
