package com.example.beikeapp.Util;

import android.graphics.Color;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherJiaXiao.JiaXiaoFragment;
import com.example.beikeapp.TeacherShiSheng.ShiShengFragment;
import com.example.beikeapp.TeacherWoDe.WoDeFragment;
import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by m1821 on 2018/4/9.
 *
 *     此处不包含BottomBar的init方法。
 *     因为需要用到的getSupportFragmentManager方法不能写在静态类中。
 *     所以调用这里的静态方法前需先做一步初始化。
 */

public class BottomBar {
    public static void setTeacherBottomBar(BottomTabBar mBottomTabBar){

        mBottomTabBar
                .setImgSize(70, 70)
                .setFontSize(14)
                .setTabPadding(5, 0, 5)
                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))

                .addTabItem("师生", R.mipmap.erweima, R.mipmap.ic_launcher, ShiShengFragment.class)
                .addTabItem("家校", R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, JiaXiaoFragment.class)
                .addTabItem("我的", R.mipmap.ic_launcher, WoDeFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));
    }
}
