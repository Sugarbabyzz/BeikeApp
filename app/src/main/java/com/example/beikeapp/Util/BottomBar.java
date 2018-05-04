package com.example.beikeapp.Util;

import android.graphics.Color;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentClass.ClassFragment;
import com.example.beikeapp.StudentNotify.NotifyFragment;
import com.example.beikeapp.TeacherChat.JiaXiaoFragment;
import com.example.beikeapp.TeacherMain.MainFragment;
import com.example.beikeapp.TeacherMy.MyFragment;
import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by m1821 on 2018/4/9.
 *
 *     此处不包含BottomBar的init方法。
 *     因为需要用到的getSupportFragmentManager方法不能写在静态类中。
 *     所以调用这里的静态方法前需先做一步初始化。
 *     参考TeacherShiSheng包下TeacherMainFunction类中19-21行代码。
 */

public class BottomBar {
    public static void setTeacherBottomBar(BottomTabBar mBottomTabBar){

        mBottomTabBar
                .setImgSize(70, 70)
                .setFontSize(14)
                .setTabPadding(5, 0, 5)
                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))

                .addTabItem("师生", R.mipmap.erweima, R.mipmap.ic_launcher, MainFragment.class)
                .addTabItem("家校", R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, JiaXiaoFragment.class)
                .addTabItem("我的", R.mipmap.ic_launcher, MyFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));
    }

    public static void setStudentBottomBar(BottomTabBar mBottomTabBar){

        mBottomTabBar
                .setImgSize(70, 70)
                .setFontSize(14)
                .setTabPadding(5, 0, 5)
                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))

                .addTabItem("首页", R.mipmap.erweima, R.mipmap.ic_launcher, com.example.beikeapp.StudentMain.MainFragment.class)
                .addTabItem("会话", R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, NotifyFragment.class)
                .addTabItem("班级", R.mipmap.ic_launcher, R.mipmap.erweima, ClassFragment.class)
                .addTabItem("我的", R.mipmap.erweima, R.mipmap.ic_launcher, com.example.beikeapp.StudentSetting.SettingFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));

    }
}
