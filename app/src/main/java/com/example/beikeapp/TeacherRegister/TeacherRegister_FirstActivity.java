package com.example.beikeapp.TeacherRegister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class TeacherRegister_FirstActivity extends BaseActivity implements View.OnClickListener {

    FormEditText etPhone;
    FormEditText etPsw;
    FormEditText etPswConfirm;
    EditText etCode;
    Button btnNext;
    Button btnGetCode;
    String phoneNumber, password;
    int i = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_first);
        initView();
    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPsw = findViewById(R.id.et_psw);
        etPswConfirm = findViewById(R.id.et_pswConfirm);
        etCode = findViewById(R.id.et_code);
        btnGetCode = findViewById(R.id.btn_getCode);
        btnNext = findViewById(R.id.btn_next);
        btnGetCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);

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
        phoneNumber = etPhone.getText().toString();
        switch (view.getId()) {
            case R.id.btn_getCode:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNumber)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNumber);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btnGetCode.setClickable(false);
                btnGetCode.setText("重新发送(" + i + ")");
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

            case R.id.btn_next:

                password = etPsw.getText().toString();
                FormEditText[] allFields = {etPhone, etPsw, etPswConfirm};
                boolean allValid = true;

                for (FormEditText field : allFields) {
                    allValid = field.testValidity() && allValid;
                }

                if (allValid) {
                    if (!password.equals(etPswConfirm.getText().toString())) {
                        Toast.makeText(TeacherRegister_FirstActivity.this, "密码不同", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        SMSSDK.submitVerificationCode("86", phoneNumber, etCode.getText().toString().trim());
                    }
                } else {
                }
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btnGetCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btnGetCode.setText("获取验证码");
                btnGetCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信验证成功，数据库查重
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功

                        isExisted(phoneNumber);

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

    //查数据库判断是否存在
    public void isExisted(String phoneNumber) {
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_ISEXISTED + "?phoneNumber=" + phoneNumber;
        new MyAsyncTask().execute(urlString);
    }

    //发起HTTP请求
    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urlString) {
            HttpURLConnection connection;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urlString[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(80000);
                connection.setReadTimeout(80000);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }


        @Override
        protected void onPostExecute(String response) {
            if (response.equals(TeacherConstant.FLAG_YES)) {
                Toast.makeText(TeacherRegister_FirstActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
            } else if (response.equals(TeacherConstant.FLAG_NO)) {
                Intent intent = new Intent(TeacherRegister_FirstActivity.this, TeacherRegister_SecondActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        }
    }
}
