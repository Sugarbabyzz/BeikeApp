package com.example.beikeapp.Util.ProfileUtil;

import android.graphics.Bitmap;

/**
 * Created by m1821 on 2018/5/19.
 * 由于Intent传递参数时参数有大小限制，过大会报错，崩溃
 * 因此只能单独写个类，用静态变量去存储要传递的数据
 * 即此处的bitmap
 */

public class BitmapStore {

    public static Bitmap bitmap;

    public static boolean isSet = false;

    public void setBitmap(Bitmap bitmap){
        BitmapStore.bitmap = bitmap;
        isSet = true;
    }

}
