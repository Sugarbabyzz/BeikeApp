package com.example.beikeapp.Util.ProfileUtil;

import android.graphics.Bitmap;

/**
 * Created by m1821 on 2018/5/19.
 * 由于Intent传递参数时参数有大小限制，过大会报错，崩溃
 * 因此只能单独写个类，用静态变量去存储要传递的数据
 * 即此处的bitmap
 */

public class ProfileInfo {

    public static boolean isSet = false;

    public static Bitmap bitmap;

    public static String name;

    public static String gender;

    public static String school;

    public static String classes;

    public void setInfoAsNonParent(String name, String gender, String school, String classes, Bitmap bitmap) {
        ProfileInfo.name = name;
        ProfileInfo.gender = gender;
        ProfileInfo.school = school;
        ProfileInfo.classes = classes;
        ProfileInfo.bitmap = bitmap;
        ProfileInfo.isSet = true;
    }

    public void setInfoAsParent(String name, String gender, Bitmap bitmap) {
        ProfileInfo.name = name;
        ProfileInfo.gender = gender;
        ProfileInfo.bitmap = bitmap;
        ProfileInfo.isSet = true;
    }

}
