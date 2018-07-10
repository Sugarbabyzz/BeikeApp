package com.example.beikeapp.TeacherMain.HomeworkComplete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.beikeapp.R;

public class HomeworkStatusDetailActivity extends AppCompatActivity {

    // homework title
    private TextView tvTitle;

    // homework error rate detail
    private TextView tvErrRate;

    // homework completion status
    private TextView tvCompletion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_check_homework_detail);

        int i = Integer.parseInt(getIntent().getStringExtra("position"));

        initView();

        tvTitle.setText(HomeworkStatus.homeworkStatusList.get(i).getTitle());
        tvErrRate.setText(HomeworkStatus.homeworkStatusList.get(i).getErrRate());
        tvCompletion.setText(HomeworkStatus.homeworkStatusList.get(i).getCompletion());

    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvErrRate = findViewById(R.id.tv_error_rate);
        tvCompletion = findViewById(R.id.tv_completion);

    }
}
