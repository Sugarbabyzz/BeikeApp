package com.example.beikeapp.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

public class StudentRegisterInfo extends BaseActivity implements View.OnClickListener{

    EditText stuName;
    RadioGroup stuSex;
    Button btnInfo;

    String account;
    String password;

    //回调参数
    private List<String> receviceData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_info);
        //获取账号与密码
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        password = intent.getStringExtra("password");
        //UI初始化
        initViews();
        //注册按钮点击事件
        btnInfo.setOnClickListener(this);
    }

    private void initViews(){
        stuName = (EditText) findViewById(R.id.student_name);
        stuSex = (RadioGroup) findViewById(R.id.student_sex);
        btnInfo = (Button) findViewById(R.id.btn_info);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_info:
                String sex = "male";
                //单选判断
                if(stuSex.getCheckedRadioButtonId() == R.id.male_rb){
                    sex = "male";
                }else if(stuSex.getCheckedRadioButtonId() == R.id.famale_rb){
                    sex = "female";
                }

                if (!stuName.getText().toString().equals("")) {
                    registerInfo(stuName.getText().toString(), sex);
                } else {
                    Toast.makeText(StudentRegisterInfo.this, "姓名、性别都不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void registerInfo(String name, String sex) {
        String registerUrlStr = StudentConstant.URL_RegisterInfo + "?name=" + name + "&sex=" + sex
                                                                  +"&account=" + account + "&password=" + password;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(registerUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                //receviceData = listData;
                Intent intent = new Intent(StudentRegisterInfo.this, StudentRegisterSuccess.class);
                startActivity(intent);
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }
}
