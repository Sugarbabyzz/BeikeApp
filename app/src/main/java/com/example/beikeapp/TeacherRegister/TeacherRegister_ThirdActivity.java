package com.example.beikeapp.TeacherRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;

public class TeacherRegister_ThirdActivity extends BaseActivity implements View.OnClickListener {

    private int flag;
    private Button btnEvent;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_third);

        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        String result = intent.getStringExtra("response");
        String[] resultArray = result.split(",");

        tvResult = findViewById(R.id.textView_result);
        btnEvent = findViewById(R.id.button_event);

        //注册成功，返回班级号
        if (resultArray[0].equals(TeacherConstant.FLAG_SUCCESS)) {
            btnEvent.setText("自动登录");
            flag = 0;
            tvResult.setText("REGISTER SUCCESS" + "\n" + resultArray[1]);
        }
        //注册失败，返回提示信息
        else if (resultArray[0].equals(TeacherConstant.FLAG_FAILURE)) {
            btnEvent.setText("重新注册");
            flag = 1;
            tvResult.setText("REGISTER FAIL");
        }
        btnEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (flag == 0) {
            startActivity(new Intent(TeacherRegister_ThirdActivity.this,
                    Teacher_MainActivity.class));
        } else if (flag == 1) {
            startActivity(new Intent(TeacherRegister_ThirdActivity.this,
                    TeacherRegister_FirstActivity.class));
        }
    }
}
