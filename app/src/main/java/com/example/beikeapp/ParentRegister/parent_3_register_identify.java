package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentRegister.StudentRegisterInfo;
import com.example.beikeapp.StudentRegister.StudentRegisterSuccess;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class parent_3_register_identify extends BaseActivity implements View.OnClickListener {
   private EditText editName;
    private RadioGroup editSex;
    private Button btn_finish;

    private String account;
    private String password;
    private String stuId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_3_register_identify);

        //获取账号与密码
        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        stuId = getIntent().getStringExtra("stuId");
        //初始化UI
        initViews();
    }


    private void initViews(){
        editName = (EditText) findViewById(R.id.student_name);
        editSex = (RadioGroup) findViewById(R.id.student_sex);
        btn_finish = (Button) findViewById(R.id.btn_info);
        //注册按钮点击事件
        btn_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_info:
                String sex = "male";
                //单选判断
                if(editSex.getCheckedRadioButtonId() == R.id.male_rb){
                    sex = "男";
                }else if(editSex.getCheckedRadioButtonId() == R.id.famale_rb){
                    sex = "女";
                }

                if (!editName.getText().toString().equals("")) {
                    registerInfo(editName.getText().toString(), sex);
                } else {
                    Toast.makeText(parent_3_register_identify.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
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
                + "&gender=" + sex
                + "&stuId=" + stuId;

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

                    Intent intent = new Intent(parent_3_register_identify.this, parent_4_register_result.class);
                    intent.putExtra("account", account);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(parent_3_register_identify.this, "信息注册失败，请重新注册！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }
}
