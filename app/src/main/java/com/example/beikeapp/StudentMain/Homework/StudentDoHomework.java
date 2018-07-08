package com.example.beikeapp.StudentMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;

public class StudentDoHomework extends AppCompatActivity {

    private static final String TAG = "StudentDoHomework";

    private int i;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temp = getIntent().getStringExtra("i");
        i = Integer.valueOf(temp);
        System.out.println("查看第i条作业：" + i);

        StudentHomework.studentHomeworkList.get(i-1).getHomeworkList().get(i-1);

        for (Homework h : StudentHomework.studentHomeworkList.get(i-1).getHomeworkList()){
            System.out.println("\n学生作业列表内容为：" + h.getSubject() + " : " + h.getOptionA() + " : " + h.getOptionB());
        }

    }
}
