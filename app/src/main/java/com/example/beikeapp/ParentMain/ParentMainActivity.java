package com.example.beikeapp.ParentMain;

import android.os.Bundle;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.BottomBar;
import com.hjm.bottomtabbar.BottomTabBar;

public class ParentMainActivity extends BaseActivity {

    private BottomTabBar btmBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载底部导航
        btmBar = findViewById(R.id.student_bottom_tab_bar);
        btmBar.init(getSupportFragmentManager(),720,1280);
        BottomBar.setParentBottomBar(btmBar);
    }

    /**
     * 用于Fragment获取BaseId
     * @return
     */
    public  String getBaseId(){
        return BaseId;
    }
}
