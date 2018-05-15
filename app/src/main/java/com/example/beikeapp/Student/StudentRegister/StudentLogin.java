package com.example.beikeapp.Student.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Student.StudentMain.StudentMain;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

public class StudentLogin extends BaseActivity implements View.OnClickListener{
    private EditText stuAccount;
    private EditText stuPassword;
    private Button btnLogin;
    private Button btnRegister;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);
        //UI初始化
        initViews();

    }

    private void initViews(){
        stuAccount = (EditText) findViewById(R.id.student_account);
        stuPassword = (EditText) findViewById(R.id.student_password);
        btnLogin = (Button) findViewById(R.id.stu_btn_login);
        btnRegister = (Button) findViewById(R.id.stu_btn_register);
        image = (ImageView) findViewById(R.id.image);
        //注册按钮点击事件
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stu_btn_login:
                if (!stuAccount.getText().toString().equals("")
                        && !stuPassword.getText().toString().equals("")) {
                    login(stuAccount.getText().toString(), stuPassword.getText().toString());
                } else {
                    Toast.makeText(StudentLogin.this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.stu_btn_register:
                startActivity(new Intent(StudentLogin.this, StudentCheckCode.class));
                break;

            default:
                break;
        }
    }

    private void login(String account, String password) {
        String loginUrlStr = StudentConstant.URL_Login + "?account=" + account + "&password=" + password;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(loginUrlStr);

        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    startActivity(new Intent(StudentLogin.this, StudentMain.class));
                }else {
                    Toast.makeText(StudentLogin.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }

}
