package com.losextraditables.bu.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.karumi.rosie.view.RosieFragment;

public abstract class BaseFragment extends RosieFragment {

  public Context getContext() {
    return getActivity();
  }

}
