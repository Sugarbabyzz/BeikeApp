package com.example.beikeapp.StudentMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.xiaomi.mipush.sdk.HWPushHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDoHomework extends AppCompatActivity {

    private static final String TAG = "StudentDoHomework";

    private int i;
    private String temp;

    public static String hwId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temp = getIntent().getStringExtra("i");
        i = Integer.valueOf(temp);
        System.out.println("查看第i条作业：" + i);

//        StudentHomework.studentHomeworkList.get(i-1);
//
//        for (Homework h : StudentHomework.studentHomeworkList.get(i-1).getHomeworkList()){
//            System.out.println("\n学生作业列表内容为：" + h.getSubject() + " : " + h.getOptionA() + " : " + h.getOptionB());
//        }

    }

    /**
     * 从数据库获取作业细节
     */
    public static void getHomeworkDetail(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getHomeworkDetail = null;
                try {
                    getHomeworkDetail = StudentConstant.getHomeworkDetailURL
                            + "?classId=" + EMClient.getInstance().groupManager().getJoinedGroupsFromServer().get(0).getGroupId()
                            + "&hwId=" + hwId;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

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

            Pattern pattern = Pattern.compile("(.*)(<subject>)(.*?)(</subject>)(.*)");
            Matcher matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                subject = m.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionA>)(.*?)(</optionA>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionA = m.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionB>)(.*?)(</optionB>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionB = m.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionC>)(.*?)(</optionC>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionC = m.group(3);
            }

            pattern = Pattern.compile("(.*)(<optionD>)(.*?)(</optionD>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                optionD = m.group(3);
            }

            pattern = Pattern.compile("(.*)(<key>)(.*?)(</key>)(.*)");
            matcher = pattern.matcher(hw);
            if (matcher.matches()) {
                key = m.group(3);
            }

            Log.d(TAG, "解析作业细节：  subject:" + subject + " optionA:" + optionA + " optionB:" + optionB + " optionC:" + optionC + " optionD:" + optionD + " key:" + key);

            //作业细节加入 List
            Homework homework = new Homework(subject, optionA, optionB, optionC, optionD, key);
            Homework.homeworkList.add(homework);
        }

    }
}
