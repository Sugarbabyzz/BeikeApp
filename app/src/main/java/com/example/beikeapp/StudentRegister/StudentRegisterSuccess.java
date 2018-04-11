package com.example.beikeapp.StudentRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;
import com.example.beikeapp.Util.BaseActivity;

public class StudentRegisterSuccess extends BaseActivity implements View.OnClickListener {

    private Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_success);

        initViews();

    }

    private void initViews(){
        btnSignin = (Button) findViewById(R.id.btn_sign_in);
        btnSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_in:
                startActivity(new Intent(StudentRegisterSuccess.this, StudentMain.class));
        }
    }


}
