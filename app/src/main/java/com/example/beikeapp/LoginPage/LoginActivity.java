package com.example.beikeapp.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherShiSheng.TeacherMainFunction;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FormEditText etAccount;
    private FormEditText etPsw;
    private Button btnLogin;
    private Button btnRegister;
    private RadioGroup rgIdentity;

    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    /**
     * 初始化view
     */
    private void initView() {
        etAccount = findViewById(R.id.et_loginAccount);
        etPsw = findViewById(R.id.et_loginPsw);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        rgIdentity = findViewById(R.id.rg_identity);
        btnLogout = findViewById(R.id.btn_logout);
        //设置按钮监听事件
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    /**
     * 所有点击事件
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //do login
            case R.id.btn_login:
                //判断字段是否为空
                FormEditText[] allFields = {etAccount,etPsw};
                boolean allValid = true;
                for (FormEditText field : allFields) {
                    allValid = field.testValidity() && allValid;
                }
                //字段无空值
                if (allValid) {
                   //三种身份选一种登录
                   int flagId = rgIdentity.getCheckedRadioButtonId();
                   if (flagId == R.id.rb_teacher){
                       loginAsTeacher(etAccount.getText().toString().trim(),etPsw.getText().toString().trim());
                   }
                   else if (flagId == R.id.rb_student){
                       loginAsStudent(etAccount.getText().toString().trim(),etPsw.getText().toString().trim());
                   }
                   else if (flagId == R.id.rb_parent){
                       loginAsParent(etAccount.getText().toString().trim(),etPsw.getText().toString().trim());
                   }
                   //未选择身份
                   else {
                       Toast.makeText(LoginActivity.this,"请选择一种身份登录",Toast.LENGTH_SHORT).show();
                   }
                }
                //字段有空值,formEditText会自动错误提示
                else {
                }
                break;
            // go register
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterGuideActivity.class));
                break;
            // forget password
            case R.id.tv_forgetPsw:
                startActivity(new Intent(LoginActivity.this, ForgetPswActivity.class));
                break;

            case R.id.btn_logout:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(true);
                    }
                }).start();

            default:
                break;
        }

    }


    /**
     * 老师身份登录
     *
     * @param account 账号
     * @param psw 密码
     */
    private void loginAsTeacher(String account, String psw) {

        //在我们的服务器中登录
        loginToUsAsTeacher(account,psw);
        //在环信服务器中登录
        loginToHx(account,psw);
    }

    /**
     * 学生身份登录
     *
     * @param account 账号
     * @param psw 密码
     */
    private void loginAsStudent(String account, String psw) {

        //在我们的服务器中登录
        loginToUsAsStudent(account,psw);
        //在环信服务器中登录
        loginToHx(account,psw);
    }

    /**
     * 家长身份登录
     *
     * @param account 账号
     * @param psw 密码
     */
    private void loginAsParent(String account, String psw) {
        loginToUsAsParent(account,psw);
        loginToHx(account,psw);
    }



    /**
     * 以老师身份登录至我们的服务器
     *
     * @param account 账号
     * @param password 密码
     */
    private void loginToUsAsTeacher(String account, String password) {

        //组装url
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_LOGIN
                + "?account=" + account
                + "&password=" + password;

        loginTask(urlString);
    }

    /**
     * 以学生身份登录至我们的服务器
     *
     * @param account 账号
     * @param psw 密码
     */
    private void loginToUsAsStudent(String account, String psw) {

        //组装url
        String urlString = "**waiting**";

        loginTask(urlString);
    }

    /**
     * 以家长身份登录至我们的服务器
     * @param account 账号
     * @param psw 密码
     */
    private void loginToUsAsParent(String account, String psw) {

        //组装url
        String urlString = "**waiting**";

        loginTask(urlString);
    }

    /**
     * 向我们登录
     * @param url 发起的http请求
     */
    private void loginTask(String url){
        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    Toast.makeText(LoginActivity.this,"login success", Toast.LENGTH_SHORT).show();
                }
                else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)){
                    Toast.makeText(LoginActivity.this,"login fail", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"login error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {

            }
        });
    }
    /**
     * 登录至环信服务器
     * 三个身份使用同一方法
     * @param account 账号
     * @param psw 密码
     */
    private void loginToHx(String account, String psw) {
        EMClient.getInstance().login(account, psw, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("TAG", "登录聊天服务器成功！");
                startActivity(new Intent(LoginActivity.this, TeacherMainFunction.class));
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
