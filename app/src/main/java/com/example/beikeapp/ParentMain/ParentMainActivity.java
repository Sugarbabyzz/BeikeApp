package com.example.beikeapp.ParentMain;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.BottomBar;
import com.hjm.bottomtabbar.BottomTabBar;

public class ParentMainActivity extends BaseActivity {

    private BottomTabBar btmBar;

    public static String stuId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_main);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //加载底部导航
        btmBar = findViewById(R.id.parent_bottom_tab_bar);
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
