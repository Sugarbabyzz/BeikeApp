package com.example.beikeapp.TeacherMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.R;

import java.util.Iterator;

public class AssignResult extends AppCompatActivity {

    private static final String TAG = "AssignResult";
    
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

    /**
     * 发送作业至服务器
     * 由服务器分发至学生
     * @param view
     */
    public void sendHomework(View view) {
        Log.d(TAG, "sendHomework: " + Homework.homeworkList.toString());
        for (Homework hw : Homework.homeworkList) {
            Log.d(TAG, "sendHomework: " + hw.getSubject());
        }
    }
}
