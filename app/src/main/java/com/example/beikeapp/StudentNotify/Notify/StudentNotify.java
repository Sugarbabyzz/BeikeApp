package com.example.beikeapp.StudentNotify.Notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.MessageReceiver;

public class StudentNotify extends AppCompatActivity implements View.OnClickListener, MessageReceiver.NotifyInter {


    private static String TAG = "StudentNotify";

    private Button btnSubmit;
    private TextView tvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notify);
        initView();
    }

    private void initView() {
        btnSubmit = findViewById(R.id.btn_notify_submit);
        tvContent = findViewById(R.id.tv_notify_content);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notify_submit:
                finish();
        }
    }

    @Override
    public void setContent(String content) {
        if (content != null){
            tvContent.setText(content);
        }
    }
}