package com.example.beikeapp.StudentMain.Homework;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.beikeapp.Adapter.NotifyAdapter;
import com.example.beikeapp.Adapter.StudentHomeworkAdapter;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentNotify.Notify.Notify;
import com.example.beikeapp.StudentNotify.Notify.StudentAllNotify;
import com.example.beikeapp.StudentNotify.Notify.StudentNotify;

public class StudentAllHomework extends AppCompatActivity {

    private ListView lvNotifyList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_homework);
        lvNotifyList = findViewById(R.id.stuhomework_list);

        //加载一次作业列表
        initView();

        swipeRefreshLayout = findViewById(R.id.stuhomework_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        //下拉刷新作业列表
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

        StudentHomeworkAdapter studentHomeworkAdapter = new StudentHomeworkAdapter(this, 1, StudentHomework.studentHomeworkList);
        lvNotifyList.setAdapter(studentHomeworkAdapter);

        lvNotifyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StudentAllHomework.this, StudentDoHomework.class);
                intent.putExtra("i", String.valueOf(i+1));
                System.out.println("查看第i条作业 ： " + i+1);

                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }

}
