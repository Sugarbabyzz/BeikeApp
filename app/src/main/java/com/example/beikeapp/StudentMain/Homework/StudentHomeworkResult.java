package com.example.beikeapp.StudentMain.Homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.Activity.ExamActivity;
import com.example.beikeapp.StudentMain.Activity.ResultActivity;
import com.example.beikeapp.StudentMain.StudentMain;

public class StudentHomeworkResult extends AppCompatActivity {


    private String title,date,time,score;
    private TextView tvTitle,tvScore,tvDate,tvTime;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework_result);

        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        date=intent.getStringExtra("date");
        time=intent.getStringExtra("time");
        score=intent.getStringExtra("score");
        tvTitle=findViewById(R.id.tv_title1);
        tvScore=findViewById(R.id.tv_score);
        tvDate=findViewById(R.id.tv_date);
        tvTime=findViewById(R.id.tv_time);
        tvTitle.setText(title);
        tvScore.append(score);
        tvDate.append(date);
        tvTime.append(time);
        setTitle("作业完成结果");
        bt=findViewById(R.id.bt_record);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( StudentHomeworkResult.this, StudentMain.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
