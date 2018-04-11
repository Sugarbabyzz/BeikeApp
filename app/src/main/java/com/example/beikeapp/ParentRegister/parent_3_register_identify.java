package com.example.beikeapp.ParentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;


public class parent_3_register_identify extends BaseActivity {
    private EditText editName;
    private EditText editSex;
    private Button btn_finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_3_register_identify);

        editName = (EditText) findViewById(R.id.parent_name);
        editSex = (EditText) findViewById(R.id.parent_sex);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString()!=null&editSex.getText().toString()!=null){
                    Toast.makeText(parent_3_register_identify.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(parent_3_register_identify.this, parent_home_page.class);
                    startActivity(intent);
                }
            }
        });

        //注册点击事件


    }
}

