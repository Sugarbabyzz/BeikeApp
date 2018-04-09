package com.example.beikeapp.Util;

import android.graphics.Color;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherChat.FirstFragment;
import com.example.beikeapp.TeacherChat.SecondFragment;
import com.example.beikeapp.TeacherChat.ThirdFragment;
import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by m1821 on 2018/4/9.
 */

public class BottomBar {
    public static void setTeacherBottomBar(BottomTabBar mBottomTabBar){

        mBottomTabBar
                .setImgSize(70, 70)
                .setFontSize(14)
                .setTabPadding(5, 0, 5)
                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))
                .addTabItem("师生", R.mipmap.erweima, R.mipmap.ic_launcher, FirstFragment.class)
                .addTabItem("家校", R.mipmap.erweima, SecondFragment.class)
                .addTabItem("我的", R.mipmap.ic_launcher, ThirdFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));
    }
}
