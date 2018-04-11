package com.example.beikeapp.Util.ChatUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class GroupDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_EDIT_GROUP_NAME = 1;
    private static final int REQUEST_CODE_EDIT_GROUP_DESCRIPTION = 2;

    private ProgressDialog progressDialog;

    private String groupId;
    private TextView tvGroupIdValue;
    private EMGroup group;
    private RelativeLayout rlAddGroupMembers;
    private RelativeLayout rlGroupNotification;
    private RelativeLayout rlChangeGroupName;
    private RelativeLayout rlChangeGroupDescription;
    private RelativeLayout rlClearConversation;
    private RelativeLayout rlGroupMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        // don't show the group if we don't find it
        if (group == null) {
            finish();
            return;
        }

        setContentView(R.layout.chat_group_details);

        initView();
    }

    private void initView() {
        tvGroupIdValue = findViewById(R.id.tv_group_id_value);
        tvGroupIdValue.setText(groupId);
        rlAddGroupMembers = findViewById(R.id.rl_add_group_members);
        rlGroupNotification = findViewById(R.id.rl_group_notification);
        rlChangeGroupName = findViewById(R.id.rl_change_group_name);
        rlChangeGroupDescription = findViewById(R.id.rl_change_group_description);
        rlClearConversation = findViewById(R.id.rl_clear_all_history);
        rlGroupMembers = findViewById(R.id.rl_group_members);

        rlAddGroupMembers.setOnClickListener(this);
        rlGroupNotification.setOnClickListener(this);
        rlChangeGroupDescription.setOnClickListener(this);
        rlChangeGroupName.setOnClickListener(this);
        rlClearConversation.setOnClickListener(this);
        rlGroupMembers.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_group_members:
                startActivity(new Intent(this, GroupDetailsMembersActivity.class)
                                .putExtra("data", group.getGroupName())
                                .putExtra("groupId",groupId));

            case R.id.rl_add_group_members:
                //later
                break;
            case R.id.rl_change_group_name:
                startActivityForResult(new Intent(this, GroupDetailsEditActivity.class)
                                .putExtra("title", "Change Group Name")
                                .putExtra("data", group.getGroupName())
                                .putExtra("editable", isCurrentOwner(group)),
                        REQUEST_CODE_EDIT_GROUP_NAME);
                break;
            case R.id.rl_change_group_description:
                startActivityForResult(new Intent(this, GroupDetailsEditActivity.class)
                                .putExtra("title", "Change Group Description")
                                .putExtra("data", group.getDescription())
                                .putExtra("editable", isCurrentOwner(group)),
                        REQUEST_CODE_EDIT_GROUP_DESCRIPTION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setMessage("don't know what this string is for..");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            switch (requestCode) {
                case REQUEST_CODE_EDIT_GROUP_NAME: //修改群名
                    final String returnData = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(returnData)) {
                        progressDialog.setMessage("changing the group name...");
                        progressDialog.show();
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().changeGroupName(groupId, returnData);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getMemberCount() + ")");
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "group name changeD!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "fail to change group name!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;
                case REQUEST_CODE_EDIT_GROUP_DESCRIPTION:
                    final String returnData1 = data.getStringExtra("data");
                    if(!TextUtils.isEmpty(returnData1)){
                        progressDialog.setMessage("changing the group description...");
                        progressDialog.show();

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().changeGroupDescription(groupId, returnData1);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "group description changeD!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "fail to change group description!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                    break;
            }
        }
    }

    /**
     * 判断操作者是否是群主
     *
     * @param group emgroup
     * @return
     */
    boolean isCurrentOwner(EMGroup group) {
        String owner = group.getOwner();
        if (owner == null || owner.isEmpty()) {
            return false;
        }
        return owner.equals(EMClient.getInstance().getCurrentUser());
    }
}
