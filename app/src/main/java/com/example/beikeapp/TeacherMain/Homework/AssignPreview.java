package com.example.beikeapp.TeacherMain.Homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AssignPreview extends AppCompatActivity {

    private static final String TAG = "AssignPreview";

    private ListView lvHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_assign_preview);

        initView();
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(this, 1, Homework.homeworkList);
        lvHomework.setAdapter(homeworkAdapter);
    }

    private void initView() {
        lvHomework = findViewById(R.id.lv_homework_list);
    }


    public void confirmToNext(View view) {
        startActivity(new Intent(this,AssignActivity.class));
    }
}
