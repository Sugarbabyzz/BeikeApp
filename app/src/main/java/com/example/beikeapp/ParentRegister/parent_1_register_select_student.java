package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;

import java.util.List;

public class parent_1_register_select_student extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "parent_1_register_selec";

    private EditText etStuName;
    private EditText etStuId;
    private Button btnCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_1_register_select_student);
        initViews();//执行初始化函数
    }
    //1、initViews()  初始化元素
    private void initViews(){
        etStuName = (EditText) findViewById(R.id.student_name);
        etStuId = (EditText) findViewById(R.id.student_id);
        btnCode = (Button) findViewById(R.id.button_select_student_info);
        btnCode.setOnClickListener(this);
    }

    //1.1中的按钮点击事件的注册
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_select_student_info:
                if (!etStuId.getText().toString().equals("")){
                    if(!etStuName.getText().toString().equals("")){
                        checkCode(etStuId.getText().toString());
                    }else{
                        Toast.makeText(parent_1_register_select_student.this, "学生姓名不能为空！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                        Toast.makeText(parent_1_register_select_student.this, "学生账号不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证学生id
     * @param stuId
     */
    private void checkCode(final String stuId){
        String checkCodeUrlStr = GlobalConstant.URL_CHECK_CODE_PARENT+ "?stuId=" + stuId ;

        Log.d(TAG, "checkCode: 验证学生id：：：" + checkCodeUrlStr);

        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(checkCodeUrlStr);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    Intent intent = new Intent(parent_1_register_select_student.this, parent_2_register_info.class);
                    intent.putExtra("stuId", stuId);
                    startActivity(intent);
                    finish();
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