package com.example.beikeapp.LoginPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherRegister.TeacherRegister_FirstActivity;
import com.example.beikeapp.TeacherRegister.TeacherRegister_SecondActivity;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPswActivity extends AppCompatActivity implements View.OnClickListener {

    private FormEditText etForgetPhone;
    private FormEditText etForgetPsw;
    private FormEditText etForgetPswConfirm;
    private FormEditText etForgetCode;
    private Button btnForgetGetCode;
    private Button btnForgetCommit;
    private RadioGroup rgFIdentity;
    private String phoneNumber;
    private String password;

    private int FLAG_CHOSEN = 0;
    private int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        initView();
    }

    private void initView() {
        etForgetPhone = findViewById(R.id.et_forgetPhone);
        etForgetPsw = findViewById(R.id.et_forgetPsw);
        etForgetPswConfirm = findViewById(R.id.et_forgetPswConfirm);
        etForgetCode = findViewById(R.id.et_forgetCode);
        rgFIdentity = findViewById(R.id.rg_fIdentity);
        btnForgetGetCode = findViewById(R.id.btn_forgetGetCode);
        btnForgetCommit = findViewById(R.id.btn_forgetCommit);
        btnForgetGetCode.setOnClickListener(this);
        btnForgetCommit.setOnClickListener(this);

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        phoneNumber = etForgetPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_forgetGetCode:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNumber)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNumber);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btnForgetGetCode.setClickable(false);
                btnForgetGetCode.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.btn_forgetCommit:

                password = etForgetPsw.getText().toString();
                if (testValidity()) {
                    if (!password.equals(etForgetPswConfirm.getText().toString())) {
                        Toast.makeText(ForgetPswActivity.this, "密码不同", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        int flagId = rgFIdentity.getCheckedRadioButtonId();
                        switch (flagId){
                            case R.id.rb_fTeacher:
                                FLAG_CHOSEN = 1;
                                SMSSDK.submitVerificationCode("86", phoneNumber, etForgetCode.getText().toString().trim());
                                break;
                            case R.id.rb_fStudent:
                                FLAG_CHOSEN = 2;
                                SMSSDK.submitVerificationCode("86", phoneNumber, etForgetCode.getText().toString().trim());
                                break;
                            case R.id.rb_fParent:
                                FLAG_CHOSEN = 3;
                                SMSSDK.submitVerificationCode("86", phoneNumber, etForgetCode.getText().toString().trim());
                                break;
                            default:
                                Toast.makeText(ForgetPswActivity.this, "请选择一种身份", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } else {
                }

                break;
        }
    }
    /**
     * 短信验证handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btnForgetGetCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btnForgetGetCode.setText("获取验证码");
                btnForgetGetCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 验证成功
                        switch (FLAG_CHOSEN){
                            case 1:
                                changePswAsTeacher(etForgetPhone.getText().toString().trim(),
                                        etForgetPsw.getText().toString().trim());
                                break;
                            case 2:
                                changePswAsStudent(etForgetPhone.getText().toString().trim(),
                                        etForgetPsw.getText().toString().trim());
                                break;
                            case 3:
                                changePswAsParent(etForgetPhone.getText().toString().trim(),
                                        etForgetPsw.getText().toString().trim());
                                break;
                            default:
                                break;
                        }

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 检测所填入的字符串的合理性
     * @return true:合理 false:不合理
     */
    private boolean testValidity(){
        FormEditText[] allFields = {etForgetPhone, etForgetPsw, etForgetPswConfirm,etForgetCode};
        boolean allValid = true;

        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
        }
        return allValid;
    }
    /**
     * 家长身份修改密码
     * @param phoneNumber 账号
     * @param password 密码
     */
    private void changePswAsParent(String phoneNumber, String password) {
        String urlString = "waitin~~";
        ChangePswTask(ForgetPswActivity.this, urlString);
    }

    /**
     * 学生身份修改密码
     *
     * @param phoneNumber 账号
     * @param password    密码
     */
    private void changePswAsStudent(String phoneNumber, String password) {
        String urlString = "waitin~~";
        ChangePswTask(ForgetPswActivity.this, urlString);
    }

    /**
     * 老师身份修改密码
     *
     * @param phoneNumber 账号
     * @param password    密码
     */
    private void changePswAsTeacher(String phoneNumber, String password) {
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_CHANGE_PSW
                + "?account=" + phoneNumber
                + "&password=" + password;
        ChangePswTask(ForgetPswActivity.this, urlString);
    }

    /**
     * 修改密码的http请求
     * @param context 上下文，用于toast
     * @param url url地址
     */
    private void ChangePswTask(Context context, String url) {
        MyAsyncTask a = new MyAsyncTask(context);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                //修改成功
                if (listData.get(0).equals(TeacherConstant.FLAG_SUCCESS)) {
                    Toast.makeText(ForgetPswActivity.this,
                            "修改成功!", Toast.LENGTH_SHORT).show();
                    Log.d("TAG","suc");
                }
                //修改失败
                else if (listData.get(0).equals(TeacherConstant.FLAG_FAILURE)) {
                    Toast.makeText(ForgetPswActivity.this,
                            "修改失败!", Toast.LENGTH_SHORT).show();
                    Log.d("TAG","fail");
                }
                //未知错误
                else {
                    Toast.makeText(ForgetPswActivity.this,
                            "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });
    }


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，
        // "\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();

    }
}
