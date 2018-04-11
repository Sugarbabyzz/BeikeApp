package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;

public class parent_home_page extends BaseActivity {
    private EditText par_account;
    private EditText par_password;

    private Button btnRegister;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_home_page);

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent_home_page.this,parent_1_register_select_student.class);
                startActivity(intent);
            }
        });


        ////////////////////////////////////////////////////denglu部分////////////////////////////////////////////////////////
        par_account = (EditText) findViewById(R.id.par_account);
        par_password = (EditText) findViewById(R.id.par_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!org.apache.commons.lang3.StringUtils.isEmpty(par_account.getText().toString())
                        && !org.apache.commons.lang3.StringUtils.isEmpty(par_password.getText().toString())) {
                    Toast.makeText(parent_home_page.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    // login(etAccount.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(parent_home_page.this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void login(String account, String password) {
        // String registerUrlStr = Constant.URL_Login + "?account=" + account + "&password=" + password;
        //new MyAsyncTask(tvResult).execute(registerUrlStr);
    }




}
