package com.losextraditables.bu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;

public class SessionStore implements SessionManager {

  private static final String SHARED = "Instagram_Preferences";
  private static final String API_ACCESS_TOKEN = "access_token";
  private static final String FIREBASE_UID = "uid";
  private static final String EMAIL = "constant";
  private static final String PASSWORD = "password";

  private final Context context;

  @Inject public SessionStore(Context context) {
    this.context = context;
  }

  private SharedPreferences.Editor initializeEditor() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.edit();
  }

  public String getAccessToken() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_ACCESS_TOKEN, null);
  }

  @Override public void setEmail(String username) {
    SharedPreferences.Editor editor = initializeEditor();
    editor.putString(EMAIL, username);
    editor.commit();
  }

  @Override public void setPassword(String password) {
    SharedPreferences.Editor editor = initializeEditor();
    editor.putString(PASSWORD, password);
    editor.commit();
  }

  @Override public String getEmail() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(EMAIL, null);
  }

  @Override public String getPassword() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(PASSWORD, null);
  }

  public Boolean hasSession() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(FIREBASE_UID, null) != null;
  }

  public void setUid(String uid) {
    SharedPreferences.Editor editor = initializeEditor();
    editor.putString(FIREBASE_UID, uid);
    editor.commit();
  }

  public String getUid() {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(FIREBASE_UID, null);
  }
}