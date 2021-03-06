package com.example.beikeapp.StudentMain.Homework;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.beikeapp.Adapter.StudentHomeworkAdapter;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.gui.layout.TitleLayout;

public class StudentAllHomework extends AppCompatActivity {


    private static String TAG = "StudentAllHomework";

    private ListView lvHomeworkList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private static String hwId;
    private static String size;
    private static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_homework);
        lvHomeworkList = findViewById(R.id.stuhomework_list);

        //加载一次作业列表
        getHomework();

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
                            getHomework();
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
        lvHomeworkList.setAdapter(studentHomeworkAdapter);

        lvHomeworkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StudentAllHomework.this, StudentBeforeDoHw.class);

                //获取点击的作业id
                hwId = StudentHomework.studentHomeworkList.get(i).getHwId();
                size = StudentHomework.studentHomeworkList.get(i).getSize();
                title = StudentHomework.studentHomeworkList.get(i).getTitle();

                intent.putExtra("hwId", hwId);
                intent.putExtra("size",size);
                intent.putExtra("title", title);
                intent.putExtra("i", String.valueOf(i + 1));
                System.out.println("查看第i条作业 ： " + i + 1 + "\t\t当前hwId：" + hwId);

                //初始化作业细节
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getHomeworkDetail();
                    }
                }).start();


                startActivity(intent);
            }
        });
    }

    /**
     * 从数据库获取作业
     */
    public static void getHomework(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getHomework = null;
                try {
                    getHomework = StudentConstant.getHomeworkURL
                            + "?classId=" + EMClient.getInstance().groupManager().getJoinedGroupsFromServer().get(0).getGroupId();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
                a.execute(getHomework);
                a.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(List<String> listData) {

                        //解析作业
                        getHomeworkList(listData.get(0));
                        Log.d(TAG, "需要解析的作业：" + listData.get(0));
                    }

                    @Override
                    public void onDataReceivedFailed() {
                    }
                });
            }
        }).start();

    }


    /**
     * 解析作业列表
     *
     * @param message
     */
    public static void getHomeworkList(String message) {

        //获取前对List进行清空
        StudentHomework.studentHomeworkList.clear();


        //处理作业   title name time size hwId

        String title = "", name = "", time = "", size = "", hwId = "";
        String hw;


        Pattern p = Pattern.compile("<homework>.*?</homework>");
        Matcher m = p.matcher(message);
        if (m.find()){
            hw = m.group();

            /*
             * 接收作业 title
             */
            Pattern pattern = Pattern.compile("(.*)(<title>)(.*?)(</title>)(.*)");
            Matcher matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                title = matcher.group(3);
            }

            /*
             * 接收作业 name
             */
            pattern = Pattern.compile("(.*)(<name>)(.*?)(</name>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                name = matcher.group(3);
            }

            /*
             * 接收作业 time
             */
            pattern = Pattern.compile("(.*)(<time>)(.*?)(</time>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                time = matcher.group(3);
            }

            /*
             * 接收作业 size
             */
            pattern = Pattern.compile("(.*)(<size>)(.*?)(</size>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                size = matcher.group(3);
            }

            /*
             * 接收作业 hwId
             */
            pattern = Pattern.compile("(.*)(<hwId>)(.*?)(</hwId>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                hwId = matcher.group(3);
            }

            Log.d(TAG, "解析作业消息：  title:" + title + "  name:" + name + "  time:" + time + "  size:" + size + "  hwId:" + hwId);

            //作业加入 List
            StudentHomework studentHomework = new StudentHomework(title, name, time, size, hwId);
            StudentHomework.studentHomeworkList.add(studentHomework);
        }

    }

    /**
     * 从数据库获取作业细节
     */
    public static void getHomeworkDetail() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getHomeworkDetail = null;

                getHomeworkDetail = StudentConstant.getHomeworkDetailURL
                        + "?hwId=" + hwId;

                MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
                a.execute(getHomeworkDetail);
                a.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(List<String> listData) {

                        //解析作业
                        getHomeworkDetailList(listData.get(0));
                        Log.d(TAG, "需要解析的作业细节：" + listData.get(0));
                    }

                    @Override
                    public void onDataReceivedFailed() {
                    }
                });
            }
        }).start();

    }


    /**
     * 解析作业列表
     *
     * @param message
     */
    public static void getHomeworkDetailList(String message) {

        //获取前对List进行清空
        Homework.homeworkList.clear();

        //处理作业   subject option key

        /*
         * 接收作业 内容
         */
        String subject = "", optionA = "", optionB = "", optionC = "", optionD = "", key = "";
        String hw;

        Pattern p = Pattern.compile("<hw>.*?</hw>");
        Matcher m = p.matcher(message);
        while (m.find()) {
            hw = m.group();

            Pattern pattern = Pattern.compile("(.*)(<hw_subject>)(.*?)(</hw_subject>)(.*)");
            Matcher matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                subject = matcher.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionA>)(.*?)(</optionA>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionA = matcher.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionB>)(.*?)(</optionB>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionB = matcher.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionC>)(.*?)(</optionC>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionC = matcher.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionD>)(.*?)(</optionD>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionD = matcher.group(3);
            }

            pattern = Pattern.compile("(.*)(<hw_key>)(.*?)(</hw_key>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                key = matcher.group(3);
            }

            Log.d(TAG, "解析作业细节：  hw_subject:" + subject + " optionA:" + optionA + " optionB:" + optionB + " optionC:" + optionC + " optionD:" + optionD + " hw_key:" + key);

            //作业细节加入 List
            Homework homework = new Homework(subject, optionA, optionB, optionC, optionD, key);
            Homework.homeworkList.add(homework);
        }

    }


    public void back(View view) {
        finish();
    }

}
