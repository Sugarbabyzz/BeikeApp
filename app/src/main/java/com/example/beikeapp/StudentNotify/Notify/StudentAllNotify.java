package com.example.beikeapp.StudentNotify.Notify;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.Adapter.NotifyAdapter;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentAllNotify extends AppCompatActivity {

    private static String TAG = "StudentAllNotify";

    private ListView lvNotifyList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_notify);
        lvNotifyList = findViewById(R.id.notify_list);

        //加载一次通知列表
        getNotify();

        initView();

        swipeRefreshLayout = findViewById(R.id.notify_swipe_layout);
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
                            getNotify();
                            initView();
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            getNotify();
//                            initView();
//                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
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
                intent.putExtra("i", String.valueOf(i + 1));
                System.out.println("查看第i条通知 ： " + i + 1);

                startActivity(intent);
            }
        });
    }


    /**
     * 从数据库获取通知
     */
    public static void getNotify() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getNotify = "";

                try {
                    getNotify = StudentConstant.getNotifyURL
                            + "?classId=" + EMClient.getInstance().groupManager().getJoinedGroupsFromServer().get(0).getGroupId();
//                      + "?classId=51519468666881";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
                a.execute(getNotify);
                a.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(List<String> listData) {

                        //解析通知
                        getNotifyList(listData.get(0));
                        Log.d("StudentAllNotify", "需要解析的通知：" + listData.get(0));
                    }

                    @Override
                    public void onDataReceivedFailed() {
                    }
                });
            }
        }).start();



    }

    /**
     * 解析通知列表
     *
     * @param message
     */
    public static void getNotifyList(String message) {

        //清空之前获取的通知，重新获取
        Notify.notifyList.clear();

        String title = "", name = "", content = "", time = "";
        String notify;

        Pattern p = Pattern.compile("<notification>.*?</notification>");
        Matcher m = p.matcher(message);
        while (m.find()) {
            notify = m.group();
            /*
             * 接收通知 title
             */
            Pattern pattern = Pattern.compile("(.*)(<title>)(.*?)(</title>)(.*)");
            Matcher matcher = pattern.matcher(notify);
            if (matcher.matches()) {
                title = matcher.group(3);
            }

            /*
             * 接收通知 name
             */
            pattern = pattern.compile("(.*)(<name>)(.*?)(</name>)(.*)");
            matcher = pattern.matcher(notify);
            if (matcher.matches()) {
                name = matcher.group(3);
            }

            /*
             * 接收通知 content
             */
            pattern = pattern.compile("(.*)(<content>)(.*?)(</content>)(.*)");
            matcher = pattern.matcher(notify);
            if (matcher.matches()) {
                content = matcher.group(3);
            }

            /*
             * 接收通知 time
             */
            pattern = pattern.compile("(.*)(<time>)(.*?)(</time>)(.*)");
            matcher = pattern.matcher(notify);
            if (matcher.matches()) {
                time = matcher.group(3);
            }

            Log.d(TAG, "获取通知为: " + title + " ; " + name + " ; " + content + " ; " + time);

            //将通知加入 list
            Notify n = new Notify(title, content, name, time);
            Notify.notifyList.add(n);
        }


    }


    public void back(View view) {
        finish();
    }
}
