package com.example.beikeapp.StudentRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.beikeapp.LoginPage.LoginActivity;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;
import com.example.beikeapp.TeacherShiSheng.TeacherMainActivity;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.ChatUtil.GroupSearchedDetailsActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class StudentRegisterSuccess extends BaseActivity implements View.OnClickListener {

    private Button btnSignin;

    String account;
    String password;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_success);

        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        code = getIntent().getStringExtra("code");

        initViews();

    }

    private void initViews(){
        btnSignin = (Button) findViewById(R.id.btn_sign_in);
        btnSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_in:
                loginToHx(account, password, code);
        }
    }

    //登录到环信服务器，并加入班级群
    private void loginToHx(String account, String psw, final String code) {
        EMClient.getInstance().login(account, psw, new EMCallBack() {
            @Override
            public void onSuccess() {

                Log.d("TAG", "登录聊天服务器成功！");
                //加入班级群
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().joinGroup(code);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                startActivity(new Intent(StudentRegisterSuccess.this , StudentMain.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TAG", "登录聊天服务器失败！");
                Log.e("errorrr",i + ":" + s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}