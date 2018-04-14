package com.example.beikeapp.Util.ChatUtil;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupSearchedDetailsActivity extends AppCompatActivity {

    private Button btn_add_group;
    private TextView tv_admin;
    private TextView tv_name;
    private TextView tv_introduction;
    private EMGroup group;
    private String groupid;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_group_searched_details);
        initView();

        group = AddGroupActivity.searchedGroup;
        if(group == null){
            return;
        }

        Log.d("tag0","0");

        String groupName = group.getGroupName();
        groupid = group.getGroupId();
        tv_name.setText(groupName);

        Log.d("tag1","1");

        if(group != null){
            showGroupDetail();
            return;
        }

        Log.d("tag2","2");

        new Thread(new Runnable() {

            public void run() {
                //get detail from server
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showGroupDetail();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    final String st1 = getResources().getString(R.string.Failed_to_get_group_chat_information);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GroupSearchedDetailsActivity.this, st1+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void initView() {
        tv_name = findViewById(R.id.name);
        tv_admin = findViewById(R.id.tv_admin);
        btn_add_group = findViewById(R.id.btn_add_to_group);
        tv_introduction = findViewById(R.id.tv_introduction);
        progressBar = findViewById(R.id.loading);

    }

    /**
     * 根据查询者是否在群内设置UI
     */
    private void showGroupDetail() {
        progressBar.setVisibility(View.INVISIBLE);
        //get group detail, and you are not in, then show join button

        if(!group.getMembers().contains(EMClient.getInstance().getCurrentUser())){
            Log.d("tag",group.getMembers().toString());
            btn_add_group.setEnabled(true);
            btn_add_group.setText("加入该群");
        }
        tv_name.setText(group.getGroupName());
        tv_admin.setText(group.getOwner());
        tv_introduction.setText(group.getDescription());
    }

    /**
     * 加群
     * @param view
     */
    public void addToGroup(View view){
        String st1 = "Adding to group...";
        final String st4 = getResources().getString(R.string.Join_the_group_chat);
        final String st5 = "加群失败!";
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().joinGroup(groupid);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(GroupSearchedDetailsActivity.this, st4, Toast.LENGTH_SHORT).show();
                            btn_add_group.setEnabled(false);
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(GroupSearchedDetailsActivity.this, st5+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 返回键
     * @param view
     */
    public void back(View view){
        finish();
    }
}
