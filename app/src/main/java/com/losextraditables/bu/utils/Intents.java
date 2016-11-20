package com.losextraditables.bu.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;

public final class Intents {

  public static boolean maybeStartActivity(Context context, Intent intent) {
    if (hasHandler(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      return false;
    }
  }

  public static boolean hasHandler(Context context, Intent intent) {
    List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(intent, 0);
    return !handlers.isEmpty();
  }

  private Intents() {
    throw new AssertionError("No instances.");
  }
}
