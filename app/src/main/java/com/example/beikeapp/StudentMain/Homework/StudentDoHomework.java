package com.example.beikeapp.StudentMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.xiaomi.mipush.sdk.HWPushHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDoHomework extends AppCompatActivity {

    private static final String TAG = "StudentDoHomework";

    private int i;
    private String temp;
    private String hwId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hwId = getIntent().getStringExtra("hwId");
        temp = getIntent().getStringExtra("i");
        i = Integer.valueOf(temp);
        System.out.println("查看第i条作业：" + i + "\t\t当前hwId：" + hwId);

    }


}
