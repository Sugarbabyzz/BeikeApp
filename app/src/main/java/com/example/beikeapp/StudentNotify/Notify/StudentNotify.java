package com.example.beikeapp.StudentNotify.Notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.MessageReceiver;

public class StudentNotify extends AppCompatActivity implements View.OnClickListener{


    private static String TAG = "StudentNotify";

    private Button btnSubmit;
    private TextView tvContent;
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvTime;
    /**
     * 获取通知的题目、老师名字、内容和时间
     */
    private String temp;
    private int i;

    private String time;
    private String name;
    private String title;
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notify);

        temp = getIntent().getStringExtra("i");
        i = Integer.valueOf(temp);
        System.out.println("查看第i条通知：" + i);

        title = Notify.notifyList.get(i-1).getTitle();
        name = Notify.notifyList.get(i-1).getName();
        content = Notify.notifyList.get(i-1).getContent();
        time = Notify.notifyList.get(i-1).getTime();

        initView();
    }

    private void initView() {
        btnSubmit = findViewById(R.id.btn_notify_submit);
        tvContent = findViewById(R.id.tv_notify_content);
        tvTitle = findViewById(R.id.tv_notify_title);
        tvName = findViewById(R.id.tv_notify_name);
        tvTime = findViewById(R.id.tv_notify_time);
        btnSubmit.setOnClickListener(this);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvName.setText(name);
        tvTime.setText(time);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notify_submit:
                finish();
        }
    }

}