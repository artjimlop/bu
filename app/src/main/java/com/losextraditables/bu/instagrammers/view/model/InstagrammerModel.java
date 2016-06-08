package com.losextraditables.bu.instagrammers.view.model;

import java.util.Comparator;

public class InstagrammerModel {

  private String userId;
  private String userName;
  private String fullName;
  private String profilePicture;
  private String bio;
  private String website;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
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

  public static Comparator<InstagrammerModel> InstagrammerComparator
      = new Comparator<InstagrammerModel>() {

    public int compare(InstagrammerModel instagrammerModel, InstagrammerModel anotherInstagrammer) {
      return instagrammerModel.getUserName().compareTo(anotherInstagrammer.getUserName());
    }

  };
}
