package com.example.beikeapp.StudentMain.Homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.beikeapp.R;

public class StudentBeforeDoHw extends AppCompatActivity {

    Button btnStart;

    private String hwId, size, title, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_before_do_hw);

        hwId = getIntent().getStringExtra("hwId");
        size = getIntent().getStringExtra("size");
        title = getIntent().getStringExtra("title");
        temp = getIntent().getStringExtra("i");

        btnStart = findViewById(R.id.btn_hw_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentBeforeDoHw.this, StudentDoHomework.class);
                intent.putExtra("hwId", hwId);
                intent.putExtra("size",size);
                intent.putExtra("title", title);
                intent.putExtra("i", temp);

                startActivity(intent);
            }
        });

    }
}
