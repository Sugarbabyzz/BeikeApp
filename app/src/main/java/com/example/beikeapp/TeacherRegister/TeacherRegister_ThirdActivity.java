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

    int flag;

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

        TextView tv = findViewById(R.id.textView_result);
        Button eventBtn = findViewById(R.id.button_event);

        if (resultArray[0].equals(TeacherConstant.FLAG_SUCCESS)){
            eventBtn.setText("自动登录");
            flag = 0;
            tv.setText("REGISTER SUCCESS" + "\n" + resultArray[1]);
        }
        else if (resultArray[0].equals(TeacherConstant.FLAG_FAILURE)){
            eventBtn.setText("重新注册");
            flag = 1;
            tv.setText("REGISTER FAIL");
        }
        eventBtn.setOnClickListener(this);
   }

    @Override
    public void onClick(View view) {
        if (flag == 0){
            startActivity(new Intent(TeacherRegister_ThirdActivity.this,Teacher_MainActivity.class));
        }
        else if (flag == 1){
            startActivity(new Intent(TeacherRegister_ThirdActivity.this,TeacherRegister_FirstActivity.class));
        }
    }
}
