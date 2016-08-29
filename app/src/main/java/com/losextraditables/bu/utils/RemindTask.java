package com.losextraditables.bu.utils;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class RemindTask extends TimerTask {

  private final Activity activity;
  private final int maxPage;
  private final Timer timer;
  private final ViewPager pager;

  private int page = 0;

  public RemindTask(Activity context, ViewPager pager, int page, Timer timer) {
    this.activity = context;
    this.maxPage = page;
    this.timer = timer;
    this.pager = pager;
  }

  @Override public void run() {
    activity.runOnUiThread(new Runnable() {
      public void run() {
        Log.d("Timer", "estoy encendido");
        if (page > maxPage) {
          page = 0;
        }
        pager.setCurrentItem(page);
        page++;
      }
    });
  }
}
