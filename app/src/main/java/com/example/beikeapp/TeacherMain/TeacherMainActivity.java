package com.example.beikeapp.TeacherMain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.BottomBar;
import com.hjm.bottomtabbar.BottomTabBar;

public class TeacherMainActivity extends BaseActivity {

    private BottomTabBar btmBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);


        //set BottomTabBar
        btmBar = findViewById(R.id.bottom_tab_bar);
        btmBar.init(getSupportFragmentManager(),720,1280);
        BottomBar.setTeacherBottomBar(btmBar);

    }

    /**
     * 用于Fragment获取BaseId
     * @return
     */
    public  String getBaseId(){
        return BaseId;
    }
}
