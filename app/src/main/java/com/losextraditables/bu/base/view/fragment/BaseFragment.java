package com.losextraditables.bu.base.view.fragment;

import android.content.Context;
import com.karumi.rosie.view.RosieFragment;

public abstract class BaseFragment extends RosieFragment {

  public Context getContext() {
    return getActivity();
  }

  public abstract void scrollListToTop();
}
