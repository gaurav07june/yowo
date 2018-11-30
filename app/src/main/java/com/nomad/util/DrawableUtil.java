package com.nomad.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class DrawableUtil {
    public static Bitmap getSizedDrawable(Context context, int height, int width, int resId){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(resId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, width, height, false);

    }
}
