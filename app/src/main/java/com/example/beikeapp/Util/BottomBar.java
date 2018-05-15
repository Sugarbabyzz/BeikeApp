package com.example.beikeapp.Util;

import android.graphics.Color;

import com.example.beikeapp.Parent.ParentClass.ParentClassFragment;
import com.example.beikeapp.Parent.ParentMain.ParentMainFragment;
import com.example.beikeapp.Parent.ParentMy.ParentMyFragment;
import com.example.beikeapp.R;
import com.example.beikeapp.Student.StudentClass.ClassFragment;
import com.example.beikeapp.Student.StudentMain.StudentMainFragment;
import com.example.beikeapp.Student.StudentNotify.NotifyFragment;
import com.example.beikeapp.Student.StudentSetting.SettingFragment;
import com.example.beikeapp.Teacher.TeacherChat.JiaXiaoFragment;
import com.example.beikeapp.Teacher.TeacherMain.TeacherMainFragment;
import com.example.beikeapp.Teacher.TeacherMy.MyFragment;
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

                .addTabItem("师生", R.mipmap.erweima, R.mipmap.ic_launcher, TeacherMainFragment.class)
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

                .addTabItem("首页", R.mipmap.erweima, R.mipmap.ic_launcher, StudentMainFragment.class)
                .addTabItem("会话", R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, NotifyFragment.class)
                .addTabItem("班级", R.mipmap.ic_launcher, R.mipmap.erweima, ClassFragment.class)
                .addTabItem("我的", R.mipmap.erweima, R.mipmap.ic_launcher, SettingFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));

    }

    public static void setParentBottomBar(BottomTabBar mBottomTabBar){

        mBottomTabBar
                .setImgSize(70, 70)
                .setFontSize(14)
                .setTabPadding(5, 0, 5)
                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))

                .addTabItem("首页", R.mipmap.erweima, R.mipmap.ic_launcher, ParentMainFragment.class)
                .addTabItem("班级", R.mipmap.ic_launcher, R.mipmap.erweima, ParentClassFragment.class)
                .addTabItem("我的", R.mipmap.erweima, R.mipmap.ic_launcher, ParentMyFragment.class)

                .isShowDivider(true)
                .setDividerColor(Color.parseColor("#FF0000"))
                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"));

    }
}
