package com.example.beikeapp.Parent.ParentRegister;

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

import com.example.beikeapp.Constant.ParentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class parent_2_register_info extends BaseActivity implements View.OnClickListener{
    private EditText editPhone;        //输入手机号
    private Button btn_verificationCode;  //"获取验证码"   按钮
    private EditText verCode;          //输入返回的验证码
    private Button btn_Commit; //点击“确认”   按钮进入下一页
    private EditText editSetPassword;
    //
    int i = 30;
    //回调参数
    private List<String> receviceData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_2_register_info);

        //匹配UI元素id
        editPhone = (EditText)findViewById(R.id.parent_phone);
        btn_verificationCode = (Button)findViewById(R.id.parentRegister_Verification_Code);
        verCode = (EditText) findViewById(R.id.ver_code);
        btn_Commit = (Button) findViewById(R.id.parent_register_commit) ;
        editSetPassword = (EditText) findViewById(R.id.parent_set_password);

        //描述UI元素交互事件
        btn_verificationCode.setOnClickListener(this);  //获取验证码
        btn_Commit.setOnClickListener(this);  //“确认”  按钮，进入下一页
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
    }  //oncreate();

    public void onClick(View view) {
        String phoneNums = editPhone.getText().toString();
        switch (view.getId()){
            case R.id.parentRegister_Verification_Code:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btn_verificationCode.setClickable(false);
                btn_verificationCode.setText("重新发送(" + i + ")");
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

            case R.id.parent_register_commit:
                if (editPhone.getText().toString().equals("")) {
                    Toast.makeText(parent_2_register_info.this, "请进行手机验证！", Toast.LENGTH_SHORT).show();
                } else if (editSetPassword.getText().toString().equals("")){
                    Toast.makeText(parent_2_register_info.this, "设置密码不能为空！", Toast.LENGTH_SHORT).show();
                } else{
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNums, verCode.getText().toString());
                    //createProgressBar();
                }
                break;
            default:
                break;
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btn_verificationCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btn_verificationCode.setText("获取验证码");
                btn_verificationCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
               Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，进入StudentRegister_Activity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                       // Toast.makeText(getApplicationContext(), "注册成功",
                          //     Toast.LENGTH_SHORT).show();
                        registerAccount(editPhone.getText().toString(), editSetPassword.getText().toString());

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                      //  Toast.makeText(getApplicationContext(), "正在获取验证码",
                       //         Toast.LENGTH_SHORT).show();
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

    private void registerAccount(String phoneNumber, String password){
        String registerAccountUrlStr = ParentConstant.URL_Register_Info +"?phoneNumber=" + phoneNumber + "&password=" + password;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(registerAccountUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                System.out.println(receviceData.toString());
                if (listData.get(0).toString().equals("[100]")){
                    Toast.makeText(parent_2_register_info.this, "账号已被注册！", Toast.LENGTH_SHORT).show();
                }else if (receviceData.toString().equals("[200]")){
                    Intent intent = new Intent(parent_2_register_info.this, parent_3_register_identify.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(parent_2_register_info.this, "注册失败！", Toast.LENGTH_SHORT).show();
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
