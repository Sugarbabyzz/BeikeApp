package com.example.beikeapp.Util.ChatUtil;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.beikeapp.R;

import java.util.ArrayList;

import mabbas007.tagsedittext.TagsEditText;

/**
 * 添加群成员
 *
 */
public class GroupDetailsAddMembersActivity extends AppCompatActivity {

    private TagsEditText etAddMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_group_details_add_members);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        Boolean editable = getIntent().getBooleanExtra("editable", false);

        etAddMember = findViewById(R.id.et_add_members_edit);
        etAddMember.setEnabled(editable);
        etAddMember.setSelection(etAddMember.length());

        findViewById(R.id.btn_add_members_save).setEnabled(editable);
    }

   public void save(View view){
       setResult(RESULT_OK, new Intent().putStringArrayListExtra("data", (ArrayList<String>) etAddMember.getTags()));
       finish();
   }

    public void back(View view) {
        finish();
    }
}
