package com.losextraditables.bu.utils;

import com.firebase.client.Firebase;

public interface FirebaseService {
  Firebase getFirebaseConnection();

  Firebase createUserReference(String uid);

  Firebase getPicturesReference(String uid);

  Firebase instagrammersReference(String uid);
}
