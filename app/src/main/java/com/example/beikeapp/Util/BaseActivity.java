package com.example.beikeapp.Util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SugarSugar on 2018/3/21.
 */

public class BaseActivity extends AppCompatActivity {

    //在基类存下用户的身份
    // teacher/student/parent
    protected static String BaseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
