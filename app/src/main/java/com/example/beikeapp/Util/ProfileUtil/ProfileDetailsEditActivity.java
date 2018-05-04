package com.example.beikeapp.Util.ProfileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.ChatUtil.ChatBaseActivity;

/**
 * 详细编辑个人资料信息。供姓名、学校、班级的编辑
 *
 */
public class ProfileDetailsEditActivity extends ChatBaseActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.my_profile_details_edit);

        editText = findViewById(R.id.et_edit);

        String title = getIntent().getStringExtra("title");
        String data = getIntent().getStringExtra("data");

        if(title != null)
            ((TextView)findViewById(R.id.tv_profile_details_edit_title)).setText(title);
        if(data != null)
            editText.setText(data);


        editText.setSelection(editText.length());

    }

    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data", editText.getText().toString()));
        finish();
    }

    public void back(View view) {
        finish();
    }
}
