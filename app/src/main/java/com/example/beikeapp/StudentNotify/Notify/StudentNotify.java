package com.example.beikeapp.StudentNotify.Notify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;

public class StudentNotify extends AppCompatActivity implements View.OnClickListener{


    private static String TAG = "StudentNotify";

    private Button btnSubmit;
    private TextView tvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_notify_notify);
        initView();
    }

    private void initView() {
        btnSubmit = findViewById(R.id.btn_notify_submit);
        tvContent = findViewById(R.id.tv_notify_content);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_notify_submit:
                finish();
        }
    }
}
