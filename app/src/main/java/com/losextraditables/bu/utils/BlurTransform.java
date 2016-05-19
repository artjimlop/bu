package com.losextraditables.bu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import com.squareup.picasso.Transformation;

public class BlurTransform implements Transformation {

  RenderScript rs;

  public BlurTransform(Context context) {
    super();
    rs = RenderScript.create(context);
  }

  @Override public Bitmap transform(Bitmap bitmap) {
    Bitmap blurredBitmap = Bitmap.createBitmap(bitmap);

    Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL,
        Allocation.USAGE_SHARED);
    Allocation output = Allocation.createTyped(rs, input.getType());

    ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    script.setInput(input);

    script.setRadius(10);

    script.forEach(output);
    output.copyTo(blurredBitmap);

    return blurredBitmap;
  }

  @Override public String key() {
    return "blur";
  }
}