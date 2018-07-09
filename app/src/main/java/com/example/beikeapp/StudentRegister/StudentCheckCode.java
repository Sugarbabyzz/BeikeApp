package com.example.beikeapp.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

public class StudentCheckCode extends BaseActivity implements View.OnClickListener{


    private EditText inviteCode;
    private Button btnCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_checkcode);

        initViews();
    }

    private void initViews(){
        inviteCode = (EditText) findViewById(R.id.code);
        btnCode = (Button) findViewById(R.id.btn_check_code);
        btnCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_check_code:
                if (!inviteCode.getText().toString().equals("")){
                    checkCode(inviteCode.getText().toString());
                }else {
                    Toast.makeText(StudentCheckCode.this, "邀请码不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void checkCode(String code){
        String checkCodeUrlStr = GlobalConstant.URL_CHECK_CODE_STUDENT + "?code=" + code;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(checkCodeUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    Intent intent = new Intent(StudentCheckCode.this, StudentRegisterAccount.class);
                    intent.putExtra("code", inviteCode.getText().toString().trim());
                    startActivity(intent);
                }else {
                    Toast.makeText(StudentCheckCode.this, "邀请码错误！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {

            }
        });
    }
}
