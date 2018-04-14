package com.example.beikeapp.Util.ChatUtil;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * 展示群成员列表
 *
 */
public class GroupDetailsMemberListActivity extends AppCompatActivity {

    private List<String> memberList;
    private EMGroup emGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_group_details_member_list);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        String title = getIntent().getStringExtra("data");
        String groupId = getIntent().getStringExtra("groupId");

        if (title != null) {
            ((TextView) findViewById(R.id.tv_group_detail_members_title)).setText(title);
        }
        //获取群成员列表
        getMemberList(groupId);
    }

    /**
     * 获取群成员列表
     * @param groupId 群ID
     */
    private void getMemberList(final String groupId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    emGroup = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                    //get members
                    memberList = emGroup.getMembers();
                    //get owner
                    memberList.add(emGroup.getOwner() + "(群主)");
                    if (memberList != null & memberList.size() >= 0) {
                        //do adapt
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView lv = findViewById(R.id.lv_group_members_list);
                                ArrayAdapter<String> mAdapter = new ArrayAdapter<>(GroupDetailsMemberListActivity.this,
                                        android.R.layout.simple_list_item_1, memberList);
                                lv.setAdapter(mAdapter);
                            }
                        });

                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void back(View view){
        finish();
    }

}
