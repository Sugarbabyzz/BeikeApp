package com.example.beikeapp.TeacherMain.Homework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Notify.TeacherMainNotify;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AssignActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AssignActivity";

    private List<EMGroup> groupList;

    private TextView tvClass;

    private Button btnChooseClass;

    private Button btnSendHomework;

    private String groupIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_assign_activity);

        getGroupsOnCreate();

        initView();

    }

    private void initView() {
        tvClass = findViewById(R.id.tv_classes);
        btnChooseClass = findViewById(R.id.btn_choose_class);
        btnSendHomework = findViewById(R.id.btn_send_homework);
        btnChooseClass.setOnClickListener(this);
        btnSendHomework.setOnClickListener(this);
    }

    /**
     * 发送作业至服务器
     * 由服务器分发至学生
     */
    public void sendHomework() {


        String url = assembleUrl();
        Log.d(TAG, "sendHomework: " + url);
        sendHomeworkTask(url);

    }

    /**
     * 联网发起请求
     *
     * @param url
     */
    private void sendHomeworkTask(String url) {
        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                Log.d(TAG, "onDataReceivedSuccess: " + listData.get(0));

                switch (listData.get(0)) {
                    case GlobalConstant.FLAG_SUCCESS:
                        Log.d(TAG, "作业发送成功");
                        Toast.makeText(AssignActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

                        break;
                    case GlobalConstant.FLAG_FAILURE:
                        Log.d(TAG, "作业发送失败");
                        Toast.makeText(AssignActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d(TAG, "作业发送失败，检查网络链接！");
                        Toast.makeText(AssignActivity.this, "网络错误,请检查网络链接", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onDataReceivedFailed() {

            }
        });
    }

    /**
     * 组装url
     *
     * @return
     */
    private String assembleUrl() {
        int size = Homework.homeworkList.size();
        String[] arr = new String[size];
        int i = 0;
        for (Homework hw : Homework.homeworkList) {
            arr[i++] = hw.getHomeworkItemForUrl();
        }
        return TeacherConstant.URL_SEND_HOMEWORK
                + "?account=" + EMClient.getInstance().getCurrentUser()
                + "&to=" + groupIdStr
                + "&size=" + size
                + "&" + StringUtils.join(arr, "&");
    }

    /**
     * 选择班级, 弹出班级列表
     */
    private void createClassDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AssignActivity.this);

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
        // false by default
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_class:
                createClassDialog();
                break;
            case R.id.btn_send_homework:
                if (groupIdStr.isEmpty()){
                    Toast.makeText(this, "班级不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendHomework();
        }
    }
}
