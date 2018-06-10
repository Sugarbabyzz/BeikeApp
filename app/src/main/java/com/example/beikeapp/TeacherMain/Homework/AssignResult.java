package com.example.beikeapp.TeacherMain.Homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Adapter.HomeworkAdapter;
import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

public class AssignResult extends AppCompatActivity {

    private static final String TAG = "AssignResult";

    private ListView lvHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_assign_result);

        initView();
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(this, 1, Homework.homeworkList);
        lvHomework.setAdapter(homeworkAdapter);
    }

    private void initView() {
        lvHomework = findViewById(R.id.lv_homework_list);
    }

    /**
     * 发送作业至服务器
     * 由服务器分发至学生
     *
     * @param view
     */
    public void sendHomework(View view) {

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

                /*switch (listData.get(0)) {
                    case GlobalConstant.FLAG_SUCCESS:
                        Log.d(TAG, "作业发送成功");
                        Toast.makeText(AssignResult.this, "发送成功", Toast.LENGTH_SHORT).show();

                        break;
                    case GlobalConstant.FLAG_FAILURE:
                        Log.d(TAG, "作业发送失败");
                        Toast.makeText(AssignResult.this, "发送失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d(TAG, "作业发送失败，检查网络链接！");
                        Toast.makeText(AssignResult.this, "网络错误,请检查网络链接", Toast.LENGTH_SHORT).show();
                        break;
                }*/
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
        return TeacherConstant.URL_BASIC + TeacherConstant.URL_SEND_HOMEWORK
                + "?size=" + size + "&" + StringUtils.join(arr, "&");
    }
}
