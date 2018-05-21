package com.example.beikeapp.Util.ProfileUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.my_setting_about_us);
    }

    public void back(View view) {
        finish();
    }
}
