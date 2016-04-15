package com.losextraditables.bu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.losextraditables.bu.login.Session;
import javax.inject.Inject;

public class InstagramSession implements Session {

  private static final String SHARED = "Instagram_Preferences";
  private static final String API_USERNAME = "username";
  private static final String API_ID = "id";
  private static final String API_NAME = "name";
  private static final String API_ACCESS_TOKEN = "access_token";

  @Inject public InstagramSession() {
  }

  public void storeAccessToken(Context context, String accessToken, String id, String username,
      String name) {
    SharedPreferences.Editor editor = initializeEditor(context);
    editor.putString(API_ID, id);
    editor.putString(API_NAME, name);
    editor.putString(API_ACCESS_TOKEN, accessToken);
    editor.putString(API_USERNAME, username);
    editor.commit();
  }

  private SharedPreferences.Editor initializeEditor(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.edit();
  }

  public void resetAccessToken(Context context) {
    SharedPreferences.Editor editor = initializeEditor(context);
    editor.putString(API_ID, null);
    editor.putString(API_NAME, null);
    editor.putString(API_ACCESS_TOKEN, null);
    editor.putString(API_USERNAME, null);
    editor.commit();
  }

  public String getUsername(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_USERNAME, null);
  }

  public String getId(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_ID, null);
  }

  public String getName(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_NAME, null);
  }

  public String getAccessToken(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_ACCESS_TOKEN, null);
  }

  public Boolean hasSession(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    return sharedPref.getString(API_ACCESS_TOKEN, null) != null;
  }
}