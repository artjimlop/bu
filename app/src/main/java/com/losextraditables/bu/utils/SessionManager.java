package com.losextraditables.bu.utils;

public interface SessionManager {
  String getUid();

  Boolean hasSession();

  void setUid(String uid);

  String getAccessToken();
}
