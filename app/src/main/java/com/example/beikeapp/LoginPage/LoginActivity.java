package com.example.beikeapp.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.ParentConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.ParentMain.ParentMainActivity;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;
import com.example.beikeapp.TeacherMain.TeacherMainActivity;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "LoginActivity";

    private FormEditText etAccount;
    private FormEditText etPsw;
    private Button btnLogin;
    private Button btnRegister;
    private RadioGroup rgIdentity;
    private TextView tvForgetPsw;

    private Button btnLogout;

    private String account,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MiPushClient.setUserAccount(LoginActivity.this,"45726929321985",null);
        Log.d(TAG,"EXED!!!!");
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
        tvForgetPsw = findViewById(R.id.tv_forgetPsw);
        //设置按钮监听事件
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tvForgetPsw.setOnClickListener(this);

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
                    //获取到用户名和密码
                    account = etAccount.getText().toString().trim();
                    password = etPsw.getText().toString().trim();

                   //三种身份选一种登录
                   int flagId = rgIdentity.getCheckedRadioButtonId();
                   switch (flagId){
                       case R.id.rb_teacher:
                           //在基类里存储身份
                           BaseId = GlobalConstant.ID_TEACHER;

                           loginAsTeacher();

                           break;
                       case R.id.rb_student:
                           //在基类里存储身份
                           BaseId = GlobalConstant.ID_STUDENT;

                           loginAsStudent();
                           break;
                       case R.id.rb_parent:
                           //在基类里存储身份
                           BaseId = GlobalConstant.ID_PARENT;

                           loginAsParent();
                           break;
                       default: //未选择身份
                           Toast.makeText(LoginActivity.this,"请选择一种身份登录",Toast.LENGTH_SHORT).show();
                           break;
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
     */
    private void loginAsTeacher() {
        //组装url
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_LOGIN
                + "?account=" + account
                + "&password=" + password;

        loginTask(urlString);
    }

    /**
     * 学生身份登录
     *
     */
    private void loginAsStudent() {

        //组装url
        String urlString = StudentConstant.URL_Login
                + "?account=" + account
                + "&password=" + password;

        loginTask(urlString);
    }

    /**
     * 家长身份登录
     *
     */
    private void loginAsParent() {
        //组装url
        String urlString = ParentConstant.URL_Login
                + "?account=" + account
                + "&password=" + password;

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
                    Log.d(TAG,"login to us success");
                    // login to HX
                    loginToHx(account,password);

                }
                else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)){
                    Log.d(TAG,"login to us fail");
                }
                else {
                    Log.d(TAG,"login to us unknown error");
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
        //环信登录命令
        EMClient.getInstance().login(account, psw, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                Log.d(TAG, "登录聊天服务器成功！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    }
                });

                //根据身份登录
                switch (BaseId){
                    case GlobalConstant.ID_TEACHER:

                        startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
                        break;
                    case GlobalConstant.ID_STUDENT:
                        MiPushClient.setUserAccount(LoginActivity.this,"45726929321985",null);
                        startActivity(new Intent(LoginActivity.this, StudentMain.class));
                        break;
                    case GlobalConstant.ID_PARENT:
                        startActivity(new Intent(LoginActivity.this, ParentMainActivity.class));
                        break;
                }
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "登录聊天服务器失败！");
                Log.e(TAG + "**ERROR**",i + ":" + s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }


    /**
     * 实现再次打开app显示上次的页面
     *
     * @return is or not root task
     */
    protected boolean initRootCheck() {
        // 判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
        if (!this.isTaskRoot()) {
            // 如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = this.getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && action != null
                    && action.equals(Intent.ACTION_MAIN)) {
                this.finish();
                return true;
            }
        }
        return false;
    }

}
