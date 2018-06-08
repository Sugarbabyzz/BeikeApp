package com.example.beikeapp.TeacherMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.R;

public class AssignResult extends AppCompatActivity {

    private HomeworkAdapter homeworkAdapter;
    private ListView lvHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_assign_result);

        initView();

        homeworkAdapter = new HomeworkAdapter(this,1,Homework.homeworkList);
        lvHomework.setAdapter(homeworkAdapter);
    }

    private void initView() {
        lvHomework = findViewById(R.id.lv_homework_list);
    }
}
