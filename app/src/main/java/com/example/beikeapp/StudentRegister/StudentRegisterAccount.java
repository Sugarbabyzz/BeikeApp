package com.example.beikeapp.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class StudentRegisterAccount extends BaseActivity implements View.OnClickListener{


    // 手机号输入框
    private EditText stuPhoneNumber;

    // 验证码输入框
    private EditText VerCode;

    // 获取验证码按钮
    private Button btn_getCode;

    // 注册按钮
    private Button btn_phone;

    private EditText stuSetPassword;

    //
    String code;
    int i = 30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_phone);

        code = getIntent().getStringExtra("code");

        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        stuPhoneNumber = (EditText) findViewById(R.id.student_phonenumber);
        VerCode = (EditText) findViewById(R.id.ver_code);
        btn_getCode = (Button) findViewById(R.id.btn_getcode);
        btn_phone = (Button) findViewById(R.id.btn_phone);
        stuSetPassword = (EditText) findViewById(R.id.stu_set_password);
        btn_getCode.setOnClickListener(this);
        btn_phone.setOnClickListener(this);


        EventHandler eventHandler = new EventHandler(){
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
    public void onClick(View v) {
        String phoneNums = stuPhoneNumber.getText().toString();
        switch (v.getId()) {
            case R.id.btn_getcode:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btn_getCode.setClickable(false);
                btn_getCode.setText("重新发送(" + i + ")");
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

            case R.id.btn_phone:
                /**
                 * testing
                 * 正式版中需要改回手机号注册！！！！！
                 */
                Intent intent = new Intent(StudentRegisterAccount.this, StudentRegisterInfo.class);
                //将账号与密码参数传入下一Actiivity
                intent.putExtra("account",stuPhoneNumber.getText().toString().trim());
                intent.putExtra("password",stuSetPassword.getText().toString().trim());
                intent.putExtra("code", code);
                startActivity(intent);

                /*if (stuPhoneNumber.getText().toString().equals("")) {
                    Toast.makeText(StudentRegisterAccount.this, "请进行手机验证！", Toast.LENGTH_SHORT).show();
                } else if (stuSetPassword.getText().toString().equals("")){
                    Toast.makeText(StudentRegisterAccount.this, "设置密码不能为空！", Toast.LENGTH_SHORT).show();
                } else{
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNums, VerCode.getText().toString());
                }*/
                break;
        }
    }

    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btn_getCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btn_getCode.setText("获取验证码");
                btn_getCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，进入StudentRegister_Activity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        registerAccount(stuPhoneNumber.getText().toString(), stuSetPassword.getText().toString());
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
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
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
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
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    /*
        账号注册
     */
    private void registerAccount(final String phoneNumber, String password){
        String registerAccountUrlStr = StudentConstant.URL_RegisterAccount +"?account=" + phoneNumber + "&password=" + password;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(registerAccountUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if (listData.get(0).equals(GlobalConstant.FLAG_YES)){
                    Toast.makeText(StudentRegisterAccount.this, "账号已被注册！", Toast.LENGTH_SHORT).show();
                }else if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    Intent intent = new Intent(StudentRegisterAccount.this, StudentRegisterInfo.class);
                    //将账号与密码参数传入下一Actiivity
                    intent.putExtra("account",stuPhoneNumber.getText().toString().trim());
                    intent.putExtra("password",stuSetPassword.getText().toString().trim());
                    intent.putExtra("code", code);
                    startActivity(intent);
                }else {
                    Toast.makeText(StudentRegisterAccount.this, "注册失败！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
