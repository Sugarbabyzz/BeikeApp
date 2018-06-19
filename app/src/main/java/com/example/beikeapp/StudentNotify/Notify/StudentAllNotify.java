package com.example.beikeapp.StudentNotify.Notify;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.Adapter.NotifyAdapter;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class StudentAllNotify extends AppCompatActivity {

    private ListView lvNotifyList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_notify);
        lvNotifyList = findViewById(R.id.notify_list);

        //加载一次通知列表
        initView();

        swipeRefreshLayout = findViewById(R.id.notify_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        //下拉刷新群组列表
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            initView();
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }

    private void initView() {

        NotifyAdapter notifyAdapter = new NotifyAdapter(this, 1, Notify.notifyList);
        lvNotifyList.setAdapter(notifyAdapter);

        lvNotifyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StudentAllNotify.this, StudentNotify.class);
                intent.putExtra("i", String.valueOf(i+1));
                System.out.println("查看第i条通知 ： " + i+1);

                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
