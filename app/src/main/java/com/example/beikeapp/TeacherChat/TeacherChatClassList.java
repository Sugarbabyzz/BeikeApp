package com.example.beikeapp.TeacherChat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;

import java.util.List;

public class TeacherChatClassList extends AppCompatActivity {

    private ListView lvClassList;

    private ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_chat_classlist);

        initView();
        //获取老师所带班级列表
        getClassList(EMClient.getInstance().getCurrentUser());


    }

    private void initView() {
        lvClassList = findViewById(R.id.lv_classList);
    }

    private void getClassList(String currentUser) {
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_GET_CLASS_LIST
                + "?account=" + currentUser;


        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(urlString);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                //listData.get(0)的数据格式: SUCCESS/六年级(2)班,四年级(3)班,五年级(7)班,...
                //                           FAIL/null
                String[] responseArray = listData.get(0).split("/");

                //获取成功
                if (responseArray[0].equals(TeacherConstant.FLAG_SUCCESS)) {
                    //获得班级列表数组并适配至ListView中
                    String[] classArray = responseArray[1].split(",");
                    mAdapter = new ArrayAdapter<>(TeacherChatClassList.this,
                            android.R.layout.simple_list_item_1,classArray);
                    lvClassList.setAdapter(mAdapter);

                }
                //获取失败，返回提示信息
                else if (responseArray[0].equals(TeacherConstant.FLAG_FAILURE)) {
                    Toast.makeText(TeacherChatClassList.this,"获取班级列表失败!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });
    }
}
