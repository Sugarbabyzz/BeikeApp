package com.example.beikeapp.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.beikeapp.ParentRegister.parent_1_register_select_student;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentRegister.StudentCheckCode;
import com.example.beikeapp.TeacherRegister.TeacherRegister_FirstActivity;


public class RegisterGuideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAsStudent;
    private Button btnAsTeacher;
    private Button btnAsParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_guide);
        initView();
    }

    private void initView() {
        btnAsStudent = findViewById(R.id.btn_asStudent);
        btnAsTeacher = findViewById(R.id.btn_asTeacher);
        btnAsParent = findViewById(R.id.btn_asParent);
        btnAsStudent.setOnClickListener(this);
        btnAsTeacher.setOnClickListener(this);
        btnAsParent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_asStudent:
                startActivity(new Intent(RegisterGuideActivity.this,
                        StudentCheckCode.class));
                break;
            case R.id.btn_asTeacher:
                startActivity(new Intent(RegisterGuideActivity.this,
                        TeacherRegister_FirstActivity.class));
                break;
            case R.id.btn_asParent:
                startActivity(new Intent(RegisterGuideActivity.this,
                        parent_1_register_select_student.class));
                break;
        }
    }
}
