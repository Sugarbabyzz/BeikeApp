package com.example.beikeapp.Util.ChatUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;

public class NewGroupActivity extends AppCompatActivity {

    EditText etGroupName;
    EditText etGroupIntro;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_new_group);

        initView();
    }

    private void initView() {
        etGroupName = findViewById(R.id.edit_group_name);
        etGroupIntro = findViewById(R.id.edit_group_introduction);
    }

    public void save(View view){
        String name = etGroupName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            new EaseAlertDialog(this, R.string.Group_name_cannot_be_empty).show();
        } else {
            // select from contact list
            startActivityForResult(new Intent(this,
                    GroupDetailsAddMembersActivity.class).putExtra("editable",true), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //new group
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("creating the group...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String groupName = etGroupName.getText().toString().trim();
                    String desc = etGroupIntro.getText().toString();
                    final ArrayList<String> newMembersList = data.getStringArrayListExtra("data");
                    final String[] membersArray = new String[newMembersList.size()];
                    newMembersList.toArray(membersArray);

                    try {
                        EMGroupOptions option = new EMGroupOptions();
                        option.maxUsers = 200;
                        String reason = "No reason behind.";
                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;

                        EMClient.getInstance().groupManager().createGroup(groupName, desc, membersArray, reason, option);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(NewGroupActivity.this,"Group created!",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(NewGroupActivity.this, "failed to create group!" + e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }

    public void back(View view){
        finish();
    }
}
