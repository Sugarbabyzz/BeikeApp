package com.example.beikeapp.Util.ChatUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beikeapp.R;

import java.util.ArrayList;

public class GroupDetailsEditActivity extends ChatBaseActivity{
    private EditText editText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.chat_group_details_edit);

        editText = findViewById(R.id.et_edit);

        String title = getIntent().getStringExtra("title");
        String data = getIntent().getStringExtra("data");
        Boolean editable = getIntent().getBooleanExtra("editable", false);

        if(title != null)
            ((TextView)findViewById(R.id.tv_group_detail_edit_title)).setText(title);
        if(data != null)
            editText.setText(data);

        editText.setEnabled(editable);
        editText.setSelection(editText.length());

        findViewById(R.id.btn_save).setEnabled(editable);

    }

    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data",editText.getText().toString()));
        finish();
    }

    public void back(View view) {
        finish();
    }
}
