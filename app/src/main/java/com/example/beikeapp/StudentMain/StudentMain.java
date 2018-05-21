package com.example.beikeapp.StudentMain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.BottomBar;
import com.hjm.bottomtabbar.BottomTabBar;

public class StudentMain extends BaseActivity{

    private BottomTabBar btmBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //加载底部导航
        btmBar = findViewById(R.id.student_bottom_tab_bar);
        btmBar.init(getSupportFragmentManager(),720,1280);
        BottomBar.setStudentBottomBar(btmBar);

    }

    /**
     * 用于Fragment获取BaseId
     * @return
     */
    public  String getBaseId(){
        return BaseId;
    }
}
