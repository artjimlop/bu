package com.losextraditables.bu.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import cn.yangbingqiang.android.parallaxviewpager.ParallaxViewPager;

public class CustomViewPager extends ParallaxViewPager {
  public CustomViewPager(Context context) {
    super(context);
  }

  public CustomViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    try {
      return super.onTouchEvent(ev);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
      return super.onInterceptTouchEvent(ev);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return false;
  }
}
