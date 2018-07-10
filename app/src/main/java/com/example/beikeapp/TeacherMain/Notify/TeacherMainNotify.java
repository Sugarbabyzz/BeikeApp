package com.example.beikeapp.TeacherMain.Notify;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Adapter.GroupAdapter;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class TeacherMainNotify extends AppCompatActivity {

    private static String TAG = "TeacherMainNotify";

    private List<EMGroup> groupList;

    private EditText etTitle;
    private EditText etContent;
    private TextView tvClass;
    private Button btnSetClass;

    private String groupIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_notify);
        getGroupsOnCreate();
        initView();
    }

    private void initView() {
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        tvClass = findViewById(R.id.et_classes);
        btnSetClass = findViewById(R.id.btn_choose_class);
        btnSetClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popup multiple choices
                createClassDialog();
            }
        });
    }

    /**
     * 选择班级, 弹出班级列表
     */
    private void createClassDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMainNotify.this);

        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("班级列表");
        // groupName list array
        final String[] itemName = new String[groupList.size()];
        //final String[] itemId = new String[groupList.size()];
        for (int i = 0; i < groupList.size(); i++) {
            itemName[i] = groupList.get(i).getGroupName();
         //   itemId[i] = groupList.get(i).getGroupId();
        }
        // checkboxes before names
        // false by defaultx
        final boolean[] checkedItems = new boolean[groupList.size()];
        // setup multiple items
        builder.setMultiChoiceItems(itemName,
                checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {

                    }
                });
        //  positive button
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder sbName = new StringBuilder();
                StringBuilder sbId = new StringBuilder();

                for (int j = 0; j < itemName.length; j++) {
                    if (checkedItems[j]) {
                        if (sbName.toString().equals("")) { //为了去掉第一个逗号
                            sbName.append(itemName[j]);
                            sbId.append(groupList.get(j).getGroupId());
                        } else {
                            sbName.append(",").append(itemName[j]);
                            sbId.append(",").append(groupList.get(j).getGroupId());
                        }
                    }
                }
                tvClass.setText(sbName.toString());
                groupIdStr = sbId.toString();
            }
        });

        //  cancel button
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.create().show();
    }

    /**
     * Get groups from hx
     */
    private void getGroupsOnCreate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        groupList = EMClient.getInstance().groupManager().getAllGroups();
        for (int i = 0; i < groupList.size(); i++) {
            Log.d(TAG, groupList.get(i).getGroupName() + ":" + groupList.get(i).getGroupId());
        }
    }

    /**
     * 发送通知
     * @param view
     */
    public void sendNotification(View view) {
        if (etTitle.getText().toString().equals("") || etContent.getText().toString().equals("") || groupIdStr.equals("")){
            Toast.makeText(TeacherMainNotify.this,"都不能为空",Toast.LENGTH_SHORT).show();
        } else {
            String url = TeacherConstant.URL_SEND_NOTIFICATION
                    + "?account=" + EMClient.getInstance().getCurrentUser()
                    + "&title=" + etTitle.getText().toString()
                    + "&content=" + etContent.getText().toString()
                    + "&to=" + groupIdStr;
            MyAsyncTask a = new MyAsyncTask(this);
            a.execute(url);
            a.setOnAsyncResponse(new AsyncResponse() {
                @Override
                public void onDataReceivedSuccess(List<String> listData) {
                    if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                        Log.d(TAG,"notification sent!!");
                        Toast.makeText(TeacherMainNotify.this,"发送成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)){
                        Log.d(TAG,"notification failed to send!!");
                        Toast.makeText(TeacherMainNotify.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG,"some unknown error:" + listData.get(0));
                        Toast.makeText(TeacherMainNotify.this,"*************!!!!!!!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onDataReceivedFailed() {

                }
            });
        }
    }
}
