package com.example.beikeapp.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class StudentRegisterInfo extends BaseActivity implements View.OnClickListener{

    EditText stuName;
    RadioGroup stuSex;
    Button btnInfo;

    String account;
    String password;
    String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_info);
        //获取账号与密码
        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        code = getIntent().getStringExtra("code");
        //UI初始化
        initViews();

    }

    private void initViews(){
        stuName = (EditText) findViewById(R.id.student_name);
        stuSex = (RadioGroup) findViewById(R.id.student_sex);
        btnInfo = (Button) findViewById(R.id.btn_info);
        //注册按钮点击事件
        btnInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_info:
                String sex = "male";
                //单选判断
                if(stuSex.getCheckedRadioButtonId() == R.id.male_rb){
                    sex = "男";
                }else if(stuSex.getCheckedRadioButtonId() == R.id.famale_rb){
                    sex = "女";
                }

                if (!stuName.getText().toString().equals("")) {
                    registerInfo(stuName.getText().toString(), sex);
                } else {
                    Toast.makeText(StudentRegisterInfo.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void registerInfo(String name, String sex) {
        String registerUrlStr = GlobalConstant.URL_REGISTER_BASIC
                + "?id=" + BaseId
                + "&account=" + account
                + "&password=" + password
                + "&name=" + name
                + "&gender=" + sex;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(registerUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if(listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){

                    //注册账号至环信
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(account, password);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    Intent intent = new Intent(StudentRegisterInfo.this, StudentRegisterSuccess.class);
                    intent.putExtra("account", account);
                    intent.putExtra("password", password);
                    intent.putExtra("code", code);
                    startActivity(intent);
                }else {
                    Toast.makeText(StudentRegisterInfo.this, "信息注册失败，请重新注册！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }
}
