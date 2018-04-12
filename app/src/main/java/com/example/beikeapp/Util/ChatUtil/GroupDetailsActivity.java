package com.example.beikeapp.Util.ChatUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.easeui.ui.EaseGroupListener;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GroupDetailsActivity";
    private static final int REQUEST_CODE_EDIT_GROUP_NAME = 1;
    private static final int REQUEST_CODE_EDIT_GROUP_DESCRIPTION = 2;
    private static final int REQUEST_CODE_ADD_GROUP_MEMBER = 3;

    private ProgressDialog progressDialog;

    private List<String> memberList = Collections.synchronizedList(new ArrayList<String>());

    private String groupId;
    private TextView tvGroupIdValue;
    private EMGroup group;
    private RelativeLayout rlAddGroupMembers;
    private RelativeLayout rlGroupNotification;
    private RelativeLayout rlChangeGroupName;
    private RelativeLayout rlChangeGroupDescription;
    private RelativeLayout rlClearConversation;
    private RelativeLayout rlGroupMembers;

    private EMPushConfigs pushConfigs;
    GroupChangeListener groupChangeListener;

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

        //get push configs
        pushConfigs = EMClient.getInstance().pushManager().getPushConfigs();

        groupChangeListener = new GroupChangeListener();
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

        setContentView(R.layout.chat_group_details);

        initView();

        updateGroup();
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

    /**
     * 所有点击事件的处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_group_members: //进入群成员列表:因为这个是无法修改，只供查看的一个列表
                                        //所以只需使用startActivity,无需ForResult
                startActivity(new Intent(this, GroupDetailsMembersActivity.class)
                                .putExtra("data", group.getGroupName())
                                .putExtra("groupId",groupId));

            case R.id.rl_add_group_members: //添加新的群成员
                startActivityForResult(new Intent(this,GroupDetailsEditActivity.class)
                                .putExtra("title","add members")
                                .putExtra("editable",isCurrentOwner(group)),
                        REQUEST_CODE_ADD_GROUP_MEMBER);
                break;
            case R.id.rl_change_group_name: //更改群名
                startActivityForResult(new Intent(this, GroupDetailsEditActivity.class)
                                .putExtra("title", "Change Group Name")
                                .putExtra("data", group.getGroupName())
                                .putExtra("editable", isCurrentOwner(group)),
                        REQUEST_CODE_EDIT_GROUP_NAME);
                break;
            case R.id.rl_change_group_description: //更改群签名
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
                case REQUEST_CODE_ADD_GROUP_MEMBER:
                    final String[] newmembers = data.getStringArrayExtra("newmembers");

                    progressDialog.setMessage("members are being added...");
                    progressDialog.show();
                    //addMembersToGroup(newmembers);
                    break;


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
                default:
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
//    /**
//     * 增加群成员
//     *
//     * @param newmembers
//     */
//    private void addMembersToGroup(final String[] newmembers) {
//        final String str = "add group members failed!";
//        new Thread(new Runnable() {
//
//            public void run() {
//                try {
//                    // 创建者调用add方法
//                    if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
//                        EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
//                    } else {
//                        // 一般成员调用invite方法
//                        EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
//                    }
//                    updateGroup();
//                    refreshMembersAdapter();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getMemberCount()
//                                    + st);
//                            progressDialog.dismiss();
//                        }
//                    });
//                } catch (final Exception e) {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), str + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (pushConfigs == null) {
                        EMClient.getInstance().pushManager().getPushConfigsFromServer();
                    }

                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);

                        memberList.clear();
                        EMCursorResult<String> result = null;
                        do {
                            // page size set to 20 is convenient for testing, should be applied to big value
                            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                                    result != null ? result.getCursor() : "",
                                    20);
                            EMLog.d(TAG, "fetchGroupMembers result.size:" + result.getData().size());
                            memberList.addAll(result.getData());
                        } while (result.getCursor() != null && !result.getCursor().isEmpty());


                    } catch (Exception e) {
                        //e.printStackTrace();  // User may have no permission for fetch mute, fetch black list operation
                    } finally {
                        memberList.remove(group.getOwner());
                    }

                }catch (Exception e) {
                    Log.d(TAG,e.getMessage());
                }

            }
        }).start();

    }

    private class GroupChangeListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            finish();
        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            finish();
        }

        @Override
        public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
            updateGroup();
        }

        @Override
        public void onMuteListRemoved(String groupId, final List<String> mutes) {
            updateGroup();
        }

        @Override
        public void onAdminAdded(String groupId, String administrator) {
            updateGroup();
        }

        @Override
        public void onAdminRemoved(String groupId, String administrator) {
            updateGroup();
        }

        @Override
        public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(GroupDetailsActivity.this, "onOwnerChanged", Toast.LENGTH_LONG).show();
                }
            });
            updateGroup();
        }

        @Override
        public void onMemberJoined(String groupId, String member) {
            EMLog.d(TAG, "onMemberJoined");
            updateGroup();
        }

        @Override
        public void onMemberExited(String groupId, String member) {
            EMLog.d(TAG, "onMemberExited");
            updateGroup();
        }



        @Override
        public void onSharedFileAdded(String groupId, final EMMucSharedFile sharedFile) {
            if(groupId.equals(GroupDetailsActivity.this.groupId)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GroupDetailsActivity.this, "Group added a share file", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void onSharedFileDeleted(String groupId, String fileId) {
            if(groupId.equals(GroupDetailsActivity.this.groupId)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GroupDetailsActivity.this, "Group deleted a share file", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
