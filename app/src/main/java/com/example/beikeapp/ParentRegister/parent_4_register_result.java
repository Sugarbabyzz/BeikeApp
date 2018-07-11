package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.beikeapp.ParentMain.ParentMainActivity;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;
import com.example.beikeapp.StudentRegister.StudentRegisterSuccess;
import com.example.beikeapp.Util.ActivityCollector;
import com.example.beikeapp.Util.BaseActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class parent_4_register_result extends BaseActivity implements View.OnClickListener {

    private Button btnSignin;

    String account;
    String password;
    String stuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_4_register_result);

        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        stuId = getIntent().getStringExtra("stuId");

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
                loginToHx(account, password, stuId);
        }
    }

    //登录到环信服务器，并加入班级群
    private void loginToHx(String account, String psw, final String stuId) {
        //环信登录
        EMClient.getInstance().login(account, psw, new EMCallBack() {
            @Override
            public void onSuccess() {

                Log.d("TAG", "登录聊天服务器成功！");

                ParentMainActivity.stuId = stuId;

                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                startActivity(new Intent(parent_4_register_result.this, ParentMainActivity.class));
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
