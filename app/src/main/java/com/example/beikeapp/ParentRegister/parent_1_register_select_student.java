package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.Constant.ParentConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

public class parent_1_register_select_student extends BaseActivity implements View.OnClickListener{

    private EditText stuName;
    private EditText stuId;
    private Button btnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_1_register_select_student);
        initViews();//执行初始化函数
    }
    //1、initViews()  初始化元素
    private void initViews(){
        stuName = (EditText) findViewById(R.id.student_name);
        stuId = (EditText) findViewById(R.id.student_id);
        btnCode = (Button) findViewById(R.id.button_select_student_info);
        btnCode.setOnClickListener(this);
    }

    //1.1中的按钮点击事件的注册
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_select_student_info:
                if (!stuId.getText().toString().equals("")){

                        checkCode(stuId.getText().toString());

                }else {
                    Toast.makeText(parent_1_register_select_student.this, "学生ID不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void checkCode(String stuId){
        String checkCodeUrlStr = ParentConstant.URL_Register_Select_Student+ "?stuID=" + stuId ;

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(checkCodeUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                if (listData.get(0).equals("[success]")){  //GlobalConstant.FLAG_SUCCES
                    Intent intent = new Intent(parent_1_register_select_student.this, parent_2_register_info.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(parent_1_register_select_student.this, "学生ID匹配错误！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDataReceivedFailed() {

            }
        });
    }
}