package com.losextraditables.bu.utils;

import com.firebase.client.Firebase;
import javax.inject.Inject;

public class FirebaseHandler implements FirebaseService{

  public static final String FIREBASE_URL = "https://buandroid.firebaseio.com";

  @Inject public FirebaseHandler() {
  }

  public Firebase getFirebaseConnection() {
    return new Firebase(FIREBASE_URL);
  }

  public Firebase createUserReference(String uid) {
    return new Firebase(FIREBASE_URL+"/users").child(uid);
  }

  public Firebase getPicturesReference(String uid) {
    return new Firebase(FIREBASE_URL+"/users").child(uid).child("pictures");
  }

  public Firebase instagrammersReference(String uid) {
    return new Firebase(FIREBASE_URL+"/users").child(uid).child("instagrammers");
  }

  @Override public Firebase getVideosReference(String uid) {
    return new Firebase(FIREBASE_URL+"/users").child(uid).child("videos");
  }
}
