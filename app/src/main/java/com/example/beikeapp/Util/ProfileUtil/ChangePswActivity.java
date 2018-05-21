package com.example.beikeapp.Util.ProfileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.example.beikeapp.R;

public class ChangePswActivity extends AppCompatActivity {

    private FormEditText etChangePsw;

    private FormEditText etChangePswConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.my_setting_change_psw);

        etChangePsw = findViewById(R.id.et_change_psw);
        etChangePswConfirm = findViewById(R.id.et_change_psw_confirm);
    }


    /**
     * submit change result
     * @param view
     */
    public void submit(View view) {
        String password = etChangePsw.getText().toString();
        if (testValidity()) {
            if (!password.equals(etChangePswConfirm.getText().toString())) {
                Toast.makeText(ChangePswActivity.this, "密码不同", Toast.LENGTH_SHORT).show();
            } else {
                setResult(RESULT_OK,new Intent().putExtra("data", password));
                finish();
            }
        }
    }

    /**
     * 检测所填入的字符串的合理性
     * @return true:合理 false:不合理
     */
    private boolean testValidity(){
        FormEditText[] allFields = {etChangePsw,etChangePswConfirm};
        boolean allValid = true;

        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
        }
        return allValid;
    }


    /**
     * 返回
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
